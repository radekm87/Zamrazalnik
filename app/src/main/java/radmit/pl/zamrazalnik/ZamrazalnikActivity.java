package radmit.pl.zamrazalnik;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import radmit.pl.zamrazalnik.activity.AddProductToFridgeActivity;
import radmit.pl.zamrazalnik.activity.DisplayAllLocationsActivity;
import radmit.pl.zamrazalnik.activity.DisplayAllProductsActivity;
import radmit.pl.zamrazalnik.domain.ZamrazalnikDbReaderHelper;
import radmit.pl.zamrazalnik.domain.bo.Miejsce;
import radmit.pl.zamrazalnik.domain.bo.Miejsce2;
import radmit.pl.zamrazalnik.rest.OsaRestClient;

public class ZamrazalnikActivity extends AppCompatActivity {

    private ListView obj;
    Spinner locationSelect;
    ZamrazalnikDbReaderHelper dbHelper;

    ArrayAdapter arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zamrazalnik);

        dbHelper = new ZamrazalnikDbReaderHelper(this);


        OsaRestClient.get("/place/search/findAllByLevelOrderByName?level=FIRST", null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                // If the response is JSONObject instead of expected JSONArray
                Type collectionType = new TypeToken<List<Miejsce2>>() {
                }.getType();
                List<Miejsce> locations = new ArrayList<>();
                try {
                    JSONArray jsonArray = response.getJSONObject("_embedded").getJSONArray("places");
                    Gson gson = new GsonBuilder().create();
                    List<Miejsce2> imageResults = gson.fromJson(jsonArray.toString(), collectionType);
                    for (Miejsce2 m : imageResults) {
                        locations.add(new Miejsce(m.getId(), m.getName()));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                ArrayAdapter locationAdapter = new ArrayAdapter(ZamrazalnikActivity.this, R.layout.spinner, locations);
                locationSelect = (Spinner) findViewById(R.id.spinnerContext);
                locationSelect.setAdapter(locationAdapter);
                locationSelect.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        Miejsce selectedItem = (Miejsce) parent.getItemAtPosition(position);
                        // Notify the selected item text
                        Toast.makeText
                                (getApplicationContext(), "Selected : " + selectedItem, Toast.LENGTH_SHORT)
                                .show();

                        ArrayList array_list = dbHelper.getAllProductsWithQuantityFromLocation(selectedItem.getId().toString());
                        arrayAdapter = new ArrayAdapter(ZamrazalnikActivity.this, android.R.layout.simple_list_item_1, array_list);
                        obj = (ListView) findViewById(R.id.listView);
                        obj.setAdapter(arrayAdapter);
                        arrayAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

                final Miejsce selectedItem = (Miejsce) locationSelect.getSelectedItem();
                ArrayList array_list = dbHelper.getAllProductsWithQuantityFromLocation(selectedItem == null ? "-1" : selectedItem.getId().toString());

                arrayAdapter = new ArrayAdapter(ZamrazalnikActivity.this, android.R.layout.simple_list_item_1, array_list);
                obj = (ListView) findViewById(R.id.listView);
                obj.setAdapter(arrayAdapter);
                obj.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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

                ((EditText) findViewById(R.id.editTextSearch)).addTextChangedListener(new TextWatcher() {


                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence cs, int start, int before, int count) {
//                ZamrazalnikActivity.this.arrayAdapter.getFilter().filter(cs);
                    }

                    // https://stackoverflow.com/questions/12142021/how-can-i-do-something-0-5-second-after-text-changed-in-my-edittext
                    @Override
                    public void afterTextChanged(Editable s) {
                        ArrayList array_list = dbHelper.getAllProductsWithQuantityFromLocation(selectedItem == null ? "-1" : selectedItem.getId().toString(), s.toString());
                        arrayAdapter = new ArrayAdapter(ZamrazalnikActivity.this, android.R.layout.simple_list_item_1, array_list);
                        obj = (ListView) findViewById(R.id.listView);
                        obj.setAdapter(arrayAdapter);
                        arrayAdapter.notifyDataSetChanged();
                    }
                });
            }
        });
//        List<Miejsce> locations = dbHelper.getAllLocations();
//        ArrayAdapter locationAdapter = new ArrayAdapter(this, R.layout.spinner, locations);
//        locationSelect = (Spinner) findViewById(R.id.spinnerContext);
//        locationSelect.setAdapter(locationAdapter);
//        locationSelect.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                Miejsce selectedItem = (Miejsce) parent.getItemAtPosition(position);
//                // Notify the selected item text
//                Toast.makeText
//                        (getApplicationContext(), "Selected : " + selectedItem, Toast.LENGTH_SHORT)
//                        .show();
//
//                ArrayList array_list = dbHelper.getAllProductsWithQuantityFromLocation(selectedItem.getId().toString());
//                arrayAdapter = new ArrayAdapter(ZamrazalnikActivity.this, android.R.layout.simple_list_item_1, array_list);
//                obj = (ListView) findViewById(R.id.listView);
//                obj.setAdapter(arrayAdapter);
//                arrayAdapter.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });

//        List<SpinnerSelectItem> lista = new ArrayList<>();
//        ArrayList<Miejsce> allLocations = dbHelper.getAllLocations();
//        for(Miejsce miejsce : allLocations) {
//            lista.add(new SpinnerSelectItem(miejsce.getId(), miejsce.getLocationName()));
//        }
//        ArrayAdapter userAdapter = new ArrayAdapter(this, R.layout.spinner, lista);
//        locationSelect = (Spinner) findViewById(R.id.spinnerContext);
//        locationSelect.setAdapter(userAdapter);

//        final Miejsce selectedItem = (Miejsce) locationSelect.getSelectedItem();
//        ArrayList array_list = dbHelper.getAllProductsWithQuantityFromLocation(selectedItem == null ? "-1" : selectedItem.getId().toString());
//
//        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, array_list);
//        obj = (ListView) findViewById(R.id.listView);
//        obj.setAdapter(arrayAdapter);
//        obj.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
//                // TODO Auto-generated method stub
//                int id_To_Search = arg2 + 1;
//
//                Bundle dataBundle = new Bundle();
//                dataBundle.putInt("id", id_To_Search);
//
////                Intent intent = new Intent(getApplicationContext(),DisplayContact.class);
////
////                intent.putExtras(dataBundle);
////                startActivity(intent);
//            }
//        });

//        ((EditText) findViewById(R.id.editTextSearch)).addTextChangedListener(new TextWatcher() {
//
//
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence cs, int start, int before, int count) {
////                ZamrazalnikActivity.this.arrayAdapter.getFilter().filter(cs);
//            }
//
//            // https://stackoverflow.com/questions/12142021/how-can-i-do-something-0-5-second-after-text-changed-in-my-edittext
//            @Override
//            public void afterTextChanged(Editable s) {
//                ArrayList array_list = dbHelper.getAllProductsWithQuantityFromLocation(selectedItem == null ? "-1" : selectedItem.getId().toString(), s.toString());
//                arrayAdapter = new ArrayAdapter(ZamrazalnikActivity.this, android.R.layout.simple_list_item_1, array_list);
//                obj = (ListView) findViewById(R.id.listView);
//                obj.setAdapter(arrayAdapter);
//                arrayAdapter.notifyDataSetChanged();
//            }
//        });

        Button bAdd = (Button) findViewById(R.id.buttonAdd);
        Button bEdit = (Button) findViewById(R.id.buttonEdit);

        bAdd.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                Intent intent = new Intent(getApplicationContext(), AddProductToFridgeActivity.class);
                intent.putExtra("LOCATION", (Miejsce) locationSelect.getSelectedItem());
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
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        switch (item.getItemId()) {
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

            if (dbHelper.takeProductFromFridge(split[0].replace("produkt=", ""), split[1].replace("ilosc=", ""), split[2].replace("miejsce=", ""))) {
                Toast.makeText(getApplicationContext(), "OK -: " + re, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), "ERROR usuwania!!! " + re, Toast.LENGTH_SHORT).show();

            }

        }
        // else continue with any other code you need in the method

    }
}
