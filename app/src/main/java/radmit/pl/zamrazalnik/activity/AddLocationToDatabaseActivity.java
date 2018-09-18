package radmit.pl.zamrazalnik.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import radmit.pl.zamrazalnik.R;
import radmit.pl.zamrazalnik.domain.ZamrazalnikDbReaderHelper;
import radmit.pl.zamrazalnik.domain.bo.Miejsce;

public class AddLocationToDatabaseActivity extends Activity {
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
                dbHelper.insertNewLocation(new Miejsce(nameProd.getText().toString()));
                Intent intent = new Intent(getApplicationContext(), DisplayAllProductsActivity.class);
                startActivity(intent);
            }
        });
    }
}

