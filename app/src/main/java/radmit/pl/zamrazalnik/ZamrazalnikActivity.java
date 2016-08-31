package radmit.pl.zamrazalnik;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;

import radmit.pl.zamrazalnik.activity.AddProductToFridgeActivity;
import radmit.pl.zamrazalnik.domain.ZamrazalnikDbReaderHelper;

public class ZamrazalnikActivity extends AppCompatActivity {

    private ListView obj;
    ZamrazalnikDbReaderHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zamrazalnik);

        dbHelper = new ZamrazalnikDbReaderHelper(this);
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

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (scanResult != null && scanResult.getContents() != null) {
            String re = scanResult.getContents();
        }
        // else continue with any other code you need in the method

    }
}