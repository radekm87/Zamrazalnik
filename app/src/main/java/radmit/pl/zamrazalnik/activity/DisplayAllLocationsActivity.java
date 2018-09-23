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
import radmit.pl.zamrazalnik.ZamrazalnikActivity;
import radmit.pl.zamrazalnik.domain.ZamrazalnikDbReaderHelper;
import radmit.pl.zamrazalnik.domain.bo.Miejsce;

public class DisplayAllLocationsActivity  extends Activity {

    ZamrazalnikDbReaderHelper dbHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_locations);
        dbHelper = new ZamrazalnikDbReaderHelper(this);

        ArrayList<String> array_list = new ArrayList<>();
        for (Miejsce miejsce : dbHelper.getAllLocations()) {
            array_list.add(miejsce.getLocationName());
        }

        ArrayAdapter arrayAdapter=new ArrayAdapter(this,android.R.layout.simple_list_item_1, array_list);
        ListView obj = (ListView)findViewById(R.id.listViewLocations);
        obj.setAdapter(arrayAdapter);

        Button bAdd = (Button)findViewById(R.id.btnSaveLocation);
        Button bCancel = (Button)findViewById(R.id.btnCancelLocation);

        bAdd.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AddLocationToDatabaseActivity.class);
                startActivity(intent);
            }
        });

        bCancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ZamrazalnikActivity.class);
                startActivity(intent);
            }
        });
    }
}