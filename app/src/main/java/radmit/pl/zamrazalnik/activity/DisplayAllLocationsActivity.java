package radmit.pl.zamrazalnik.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import radmit.pl.zamrazalnik.R;
import radmit.pl.zamrazalnik.ZamrazalnikActivity;
import radmit.pl.zamrazalnik.domain.ZamrazalnikDbReaderHelper;
import radmit.pl.zamrazalnik.domain.bo.Miejsce;
import radmit.pl.zamrazalnik.domain.bo.Miejsce2;
import radmit.pl.zamrazalnik.rest.OsaRestClient;

public class DisplayAllLocationsActivity  extends Activity {

    ZamrazalnikDbReaderHelper dbHelper;
    ListView obj ;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_locations);
        dbHelper = new ZamrazalnikDbReaderHelper(this);

        obj = (ListView)findViewById(R.id.listViewLocations);


        final ArrayList<String> array_list = new ArrayList<>();
        OsaRestClient.get("/place/search/findAllByLevelOrderByName?level=FIRST", null, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                // If the response is JSONObject instead of expected JSONArray
                Type collectionType = new TypeToken<List<Miejsce2>>(){}.getType();
                JSONArray jsonArray = null;
                try {
                    jsonArray = response.getJSONObject("_embedded").getJSONArray("places");
                    Gson gson = new GsonBuilder().create();
                    List<Miejsce2> imageResults = gson.fromJson(jsonArray.toString(), collectionType);
                    for(Miejsce2 m : imageResults) {
                        array_list.add(m.getName());
                    }

                    ArrayAdapter arrayAdapter=new ArrayAdapter(DisplayAllLocationsActivity.this,android.R.layout.simple_list_item_1, array_list);
//                    ListView obj = (ListView)findViewById(R.id.listViewLocations);
                    obj.setAdapter(arrayAdapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray timeline) {
                // Pull out the first event on the public timeline
                JSONObject firstEvent = null;
                try {
                    firstEvent = (JSONObject) timeline.get(0);
                    String tweetText = firstEvent.getString("text");
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                // Do something with the response
                System.out.println("OOOKK");



            }
        });
//        for (Miejsce miejsce : dbHelper.getAllLocations()) {
//            array_list.add(miejsce.getLocationName());
//        }

//        ArrayAdapter arrayAdapter=new ArrayAdapter(this,android.R.layout.simple_list_item_1, array_list);
//        ListView obj = (ListView)findViewById(R.id.listViewLocations);
//        obj.setAdapter(arrayAdapter);

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