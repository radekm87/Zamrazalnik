package radmit.pl.zamrazalnik.domain;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import radmit.pl.zamrazalnik.domain.bo.Miejsce;
import radmit.pl.zamrazalnik.domain.bo.Miejsce2;
import radmit.pl.zamrazalnik.domain.bo.Produkt;
import radmit.pl.zamrazalnik.rest.OsaRestClient;

/**
 * Created by rmorawski on 31.08.16.
 */
public class ZamrazalnikRestReaderHelper {


    public ZamrazalnikRestReaderHelper() {
//        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    public ArrayList<Miejsce> getAllLocations() {
        final ArrayList<Miejsce> array_list = new ArrayList<Miejsce>();


        OsaRestClient.get("/place", null, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                // If the response is JSONObject instead of expected JSONArray
                Type collectionType = new TypeToken<List<Miejsce2>>() {
                }.getType();
                JSONArray jsonArray = null;
                try {
                    jsonArray = response.getJSONObject("_embedded").getJSONArray("places");
                    Gson gson = new GsonBuilder().create();
                    List<Miejsce2> imageResults = gson.fromJson(jsonArray.toString(), collectionType);
                    for (Miejsce2 m : imageResults) {
                        array_list.add(new Miejsce(m.getId(), m.getName()));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        return array_list;
    }


    public RequestParams addNewLocationParams(Miejsce location) {
        RequestParams params = new RequestParams();
        params.put("name", location.getLocationName());
        params.put("level", location.getLevelEnumLocation().name());
        params.setUseJsonStreamer(true);

        return params;
    }


    public RequestParams addNewProductParams(Produkt produkt) {
        RequestParams params = new RequestParams();
        params.put("name",produkt.getName());
        params.put("description", produkt.getDescription());
        params.setUseJsonStreamer(true);

        return params;
    }
}
