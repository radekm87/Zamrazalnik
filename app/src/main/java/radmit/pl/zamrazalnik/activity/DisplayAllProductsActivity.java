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
import java.util.List;
import java.util.stream.Collectors;

import cz.msebera.android.httpclient.Header;
import radmit.pl.zamrazalnik.R;
import radmit.pl.zamrazalnik.domain.bo.Produkt;
import radmit.pl.zamrazalnik.rest.OsaRestClient;

/**
 * Created by rmorawski on 02.09.16.
 */
public class DisplayAllProductsActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_products);

        OsaRestClient.get("/item", null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                fillSpinnerProducts(response);
            }
        });

        Button bAdd = (Button) findViewById(R.id.btnAddNewOnlyProduct);
        Button bCancel = (Button) findViewById(R.id.btnCancel);
        bAdd.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AddProductToDatabaseActivity.class);
                startActivity(intent);
            }
        });
        bCancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void fillSpinnerProducts(JSONObject response) {
        Type collectionType = new TypeToken<List<Produkt>>() {
        }.getType();
        try {
            JSONArray jsonArray = response.getJSONObject("_embedded").getJSONArray("items");
            Gson gson = new GsonBuilder().create();
            List<String> namesItem = ((List<Produkt>) gson.fromJson(jsonArray.toString(), collectionType)).stream().map(Produkt::getName).collect(Collectors.toList());

            ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, namesItem);
            ListView obj = (ListView) findViewById(R.id.listViewProducts);
            obj.setAdapter(arrayAdapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}