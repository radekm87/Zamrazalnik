package radmit.pl.zamrazalnik.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;

import radmit.pl.zamrazalnik.R;
import radmit.pl.zamrazalnik.ZamrazalnikActivity;
import radmit.pl.zamrazalnik.domain.ZamrazalnikDbReaderHelper;

/**
 * Created by rmorawski on 31.08.16.
 */
public class AddProductToFridgeActivity extends Activity {

    TextView name;
    TextView quantity;
    ZamrazalnikDbReaderHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);
        dbHelper = new ZamrazalnikDbReaderHelper(this);

        name = (TextView) findViewById(R.id.editText);
        quantity = (TextView) findViewById(R.id.editText2);

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

    private void saveRecordToDatabaseAndGenerateQrCode() {
        if(dbHelper.insertProduct(name.getText().toString(), Integer.parseInt(quantity.getText().toString()))){
            Toast.makeText(getApplicationContext(), "done", Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(getApplicationContext(), "not done", Toast.LENGTH_SHORT).show();
        }

        Intent intent = new Intent(getApplicationContext(),ZamrazalnikActivity.class);
        startActivity(intent);
    }

}
