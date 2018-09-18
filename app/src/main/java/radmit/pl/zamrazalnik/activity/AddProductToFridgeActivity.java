package radmit.pl.zamrazalnik.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.creativityapps.gmailbackgroundlibrary.BackgroundMail;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import radmit.pl.zamrazalnik.R;
import radmit.pl.zamrazalnik.SpinnerSelectItem;
import radmit.pl.zamrazalnik.ZamrazalnikActivity;
import radmit.pl.zamrazalnik.domain.bo.Miejsce;
import radmit.pl.zamrazalnik.domain.bo.Produkt;
import radmit.pl.zamrazalnik.domain.ZamrazalnikDbReaderHelper;

/**
 * Created by rmorawski on 31.08.16.
 */
public class AddProductToFridgeActivity extends Activity {

//    TextView name;
    TextView quantity;
    Spinner productSelect;
    Spinner locationSelect;
    ZamrazalnikDbReaderHelper dbHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_put_product_to_fridge);
        dbHelper = new ZamrazalnikDbReaderHelper(this);

//        productSelect = (Spinner) findViewById(R.id.spinnerProduct);
        quantity = (TextView) findViewById(R.id.editText2);



        // Gets all users but replace with whatever list of users you want.
        List<Miejsce> locations = dbHelper.getAllLocations();
        //simple_spinner_dropdown_item
        ArrayAdapter locationAdapter = new ArrayAdapter(this, R.layout.spinner, locations);
        locationSelect = (Spinner) findViewById(R.id.spinnerLocation);
        locationSelect.setAdapter(locationAdapter);


        // Gets all users but replace with whatever list of users you want.
        List<SpinnerSelectItem> productsList = prepareProductSelectItemList();

        ArrayAdapter userAdapter = new ArrayAdapter(this, R.layout.spinner, productsList);
        productSelect = (Spinner) findViewById(R.id.spinnerProduct);
        productSelect.setAdapter(userAdapter);

        Button btnReturn = (Button)findViewById(R.id.buttonReturn);
        btnReturn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });

        Button btnSave = (Button) findViewById(R.id.buttonSave);
        btnSave.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                saveRecordToDatabaseAndGenerateQrCode();
            }
        });
    }

    private List<SpinnerSelectItem> prepareProductSelectItemList() {
        ArrayList<Produkt> allProducts = dbHelper.getAllProducts();
        ArrayList<SpinnerSelectItem> selectItems = new ArrayList<>();
        for (Produkt prod : allProducts) {
            SpinnerSelectItem si = new SpinnerSelectItem(prod.getId(),prod.getProductName());
            selectItems.add(si);
        }

        return selectItems;
    }

    private void saveRecordToDatabaseAndGenerateQrCode() {
        final SpinnerSelectItem selectProduct = (SpinnerSelectItem) productSelect.getSelectedItem();
        // And to get the actual User object that was selected, you can do this.
        final Miejsce location = (Miejsce) ( locationSelect ).getSelectedItem();

        if(dbHelper.insertProductToFridge(selectProduct.getId(), location.getId(), Integer.valueOf(quantity.getText().toString()))){
            Toast.makeText(getApplicationContext(), "done", Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(getApplicationContext(), "not done", Toast.LENGTH_SHORT).show();
        }

        final ImageView imgView = (ImageView) findViewById(R.id.imgQrCode);
        QRCodeWriter writer = new QRCodeWriter();
        try {
            BitMatrix bitMatrix = writer.encode("produkt=" + selectProduct.getId() + ";ilosc=" + quantity.getText().toString() + ";miejsce=" + location.getId()+";", BarcodeFormat.QR_CODE, 150, 150);
            int width = bitMatrix.getWidth();
            int height = bitMatrix.getHeight();
            Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    bmp.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
                }
            }
            imgView.setImageBitmap(bmp);

        } catch (WriterException e) {
            e.printStackTrace();
        }


//        imgView.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View view) {
//                Intent intent = new Intent(getApplicationContext(),ZamrazalnikActivity.class);
//                startActivity(intent);
//            }
//        });
        imgView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Document document = new Document();

                try {
//                    String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/zamrazalnik";



                    File path2 = Environment.getExternalStoragePublicDirectory(
                            Environment.DIRECTORY_DOWNLOADS);
//                    File file = new File(path, "DemoPicture.jpg");



//                    File dir = new File(path);
//                    if(!dir.exists())
//                        dir.mkdirs();

//                    Log.d("PDFCreator", "PDF Path: " + path);


//                    File file = new File(dir, "sample.pdf");
                    File file = new File(path2, "sample.pdf");
//                    file.createNewFile();

                    PdfWriter.getInstance(document, new FileOutputStream(file));

                    document.open();

                    Drawable d = imgView.getDrawable();
                    BitmapDrawable bitDw = ((BitmapDrawable) d);
                    Bitmap bmp = bitDw.getBitmap();
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bmp.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                    Image image = Image.getInstance(stream.toByteArray());
                    document.add(image);

                    Paragraph p = new Paragraph(selectProduct.getValue());
                    document.add(p);

                    BackgroundMail.newBuilder(getApplicationContext())
                            .withUsername("magdalena.chmurzynska@gmail.com")
                            .withPassword("Onomato peja")
                            .withMailto("radekm87@gmail.com")
                            .withSubject("Test subject")
                            .withBody("this is the body")
                            .withOnSuccessCallback(new BackgroundMail.OnSuccessCallback() {
                                @Override
                                public void onSuccess() {
                                    Toast.makeText(getApplicationContext(), "OK SEND MAIL: ", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .withOnFailCallback(new BackgroundMail.OnFailCallback() {
                                @Override
                                public void onFail() {
                                    Toast.makeText(getApplicationContext(), "FAILED SEND MAIL: ", Toast.LENGTH_SHORT).show();
                                }
                            }).withAttachments(file.getAbsolutePath())
                            .send();
//                    Intent intent = new Intent(Intent.ACTION_SEND ,Uri.parse("mailto:")); // it's not ACTION_SEND
//                    intent.setType("text/plain");
//                    intent.putExtra(Intent.EXTRA_SUBJECT, "Card Set ");
//                    intent.putExtra(Intent.EXTRA_TEXT, "");
////                    intent.putExtra(Intent.EXTRA_STREAM,Uri.fromFile(file));
//                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // this will make such that when user returns to your app, your app is displayed, instead of the email app.
//                    startActivity(intent);
                }
                catch(DocumentException de) {
                    Toast.makeText(getApplicationContext(), "Exception: " + de.getMessage(), Toast.LENGTH_SHORT).show();
                    de.printStackTrace();
                } catch (MalformedURLException e) {
                    Toast.makeText(getApplicationContext(), "Exception: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                } catch (IOException e) {
                    Toast.makeText(getApplicationContext(), "Exception: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                } finally {
                    document.close();
                }



                Intent intent2 = new Intent(getApplicationContext(),ZamrazalnikActivity.class);
                startActivity(intent2);
            }
        });


    }

}
