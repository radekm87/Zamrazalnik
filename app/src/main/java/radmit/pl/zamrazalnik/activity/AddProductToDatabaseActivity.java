package radmit.pl.zamrazalnik.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


import radmit.pl.zamrazalnik.R;
import radmit.pl.zamrazalnik.domain.bo.Produkt;
import radmit.pl.zamrazalnik.domain.ZamrazalnikDbReaderHelper;

/**
 * Created by rmorawski on 02.09.16.
 */
public class AddProductToDatabaseActivity extends Activity {
    ZamrazalnikDbReaderHelper dbHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);
        dbHelper = new ZamrazalnikDbReaderHelper(this);

        final TextView nameProd = (TextView) findViewById(R.id.txtNameProduct);
        Button bSave = (Button)findViewById(R.id.btnSaveProduct);

        bSave.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dbHelper.insertNewProduct(new Produkt(nameProd.getText().toString()));
                finish();
            }
        });
    }
}
