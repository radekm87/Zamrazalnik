package radmit.pl.zamrazalnik.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

import radmit.pl.zamrazalnik.R;
import radmit.pl.zamrazalnik.domain.ZamrazalnikDbReaderHelper;

/**
 * Created by rmorawski on 02.09.16.
 */
public class DisplayAllProductsActivity  extends Activity {

    ZamrazalnikDbReaderHelper dbHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_products);
        dbHelper = new ZamrazalnikDbReaderHelper(this);

        ArrayList array_list = dbHelper.getAllProducts();

        ArrayAdapter arrayAdapter=new ArrayAdapter(this,android.R.layout.simple_list_item_1, array_list);
        ListView obj = (ListView)findViewById(R.id.listView);
        obj.setAdapter(arrayAdapter);

        Button bAdd = (Button)findViewById(R.id.btnAddNewOnlyProduct);

        bAdd.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AddProductToDatabaseActivity.class);
                startActivity(intent);
            }
        });
    }
}