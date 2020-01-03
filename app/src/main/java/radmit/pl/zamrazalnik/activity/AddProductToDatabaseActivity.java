package radmit.pl.zamrazalnik.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;


import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.util.List;

import cz.msebera.android.httpclient.Header;
import radmit.pl.zamrazalnik.R;
import radmit.pl.zamrazalnik.domain.ZamrazalnikRestReaderHelper;
import radmit.pl.zamrazalnik.domain.bo.Miejsce;
import radmit.pl.zamrazalnik.domain.bo.Produkt;
import radmit.pl.zamrazalnik.domain.ZamrazalnikDbReaderHelper;
import radmit.pl.zamrazalnik.rest.OsaRestClient;

/**
 * Created by rmorawski on 02.09.16.
 */
public class AddProductToDatabaseActivity extends Activity {
    TextView nameProd;
    EditText descProd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        nameProd = (TextView) findViewById(R.id.txtNameProduct);
        descProd = (EditText) findViewById(R.id.editText);
        Button bSave = (Button)findViewById(R.id.btnSaveProduct);

        bSave.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                addNewProduct();
            }
        });
    }

    private void addNewProduct() {
        ZamrazalnikRestReaderHelper helper = new ZamrazalnikRestReaderHelper();
        RequestParams params = helper.addNewProductParams(new Produkt(nameProd.getText().toString(), descProd.getText().toString()));

        OsaRestClient.post("/item", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                System.out.println("OK - Dodany produkt");
                Intent intent = new Intent(getApplicationContext(), DisplayAllProductsActivity.class);
                startActivity(intent);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                System.out.println("BLOND - produkt nie dodany");
            }
        });
    }
}
