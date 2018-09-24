package radmit.pl.zamrazalnik;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;
import java.util.List;

import radmit.pl.zamrazalnik.activity.AddProductToFridgeActivity;
import radmit.pl.zamrazalnik.activity.DisplayAllLocationsActivity;
import radmit.pl.zamrazalnik.activity.DisplayAllProductsActivity;
import radmit.pl.zamrazalnik.domain.ZamrazalnikDbReaderHelper;
import radmit.pl.zamrazalnik.domain.bo.Miejsce;

public class ZamrazalnikActivity extends AppCompatActivity {

    private ListView obj;
    Spinner locationSelect;
    ZamrazalnikDbReaderHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zamrazalnik);

        dbHelper = new ZamrazalnikDbReaderHelper(this);

        List<Miejsce> locations = dbHelper.getAllLocations();
        ArrayAdapter locationAdapter = new ArrayAdapter(this, R.layout.spinner, locations);
        locationSelect = (Spinner) findViewById(R.id.spinnerContext);
        locationSelect.setAdapter(locationAdapter);

        ArrayList array_list = dbHelper.getAllProductsWithQuantity();

        ArrayAdapter arrayAdapter=new ArrayAdapter(this,android.R.layout.simple_list_item_1, array_list);
        obj = (ListView)findViewById(R.id.listView);
        obj.setAdapter(arrayAdapter);
        obj.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                // TODO Auto-generated method stub
                int id_To_Search = arg2 + 1;

                Bundle dataBundle = new Bundle();
                dataBundle.putInt("id", id_To_Search);

//                Intent intent = new Intent(getApplicationContext(),DisplayContact.class);
//
//                intent.putExtras(dataBundle);
//                startActivity(intent);
            }
        });

        Button bAdd = (Button)findViewById(R.id.buttonAdd);
        Button bEdit = (Button)findViewById(R.id.buttonEdit);

        bAdd.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                Intent intent = new Intent(getApplicationContext(), AddProductToFridgeActivity.class);
                startActivity(intent);
            }
        });

        bEdit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
//                Intent intent = new Intent(getApplicationContext(), AddProductToFridgeActivity.class);
//                startActivity(intent);
                IntentIntegrator.initiateScan(ZamrazalnikActivity.this);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        super.onOptionsItemSelected(item);

        switch(item.getItemId())
        {
            case R.id.item1:
                Bundle dataBundle = new Bundle();
                dataBundle.putInt("id", 0);

//                Intent intent = new Intent(getApplicationContext(),DisplayContact.class);
//                intent.putExtras(dataBundle);
//
//                startActivity(intent);
                return true;
            case R.id.item2:
                Intent intent = new Intent(getApplicationContext(), DisplayAllProductsActivity.class);
                startActivity(intent);
                return true;
            case R.id.itemLocation:
                Intent intent2 = new Intent(getApplicationContext(), DisplayAllLocationsActivity.class);
                startActivity(intent2);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (scanResult != null && scanResult.getContents() != null) {
            String re = scanResult.getContents();

            String[] split = re.split(";");
            if (split.length != 3) {
                // error
            }

            if (dbHelper.takeProductFromFridge(split[0].replace("produkt=",""), split[1].replace("ilosc=",""), split[2].replace("miejsce=",""))) {
                Toast.makeText(getApplicationContext(), "OK -: " + re, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), "ERROR usuwania!!! " + re, Toast.LENGTH_SHORT).show();

            }

        }
        // else continue with any other code you need in the method

    }
}
