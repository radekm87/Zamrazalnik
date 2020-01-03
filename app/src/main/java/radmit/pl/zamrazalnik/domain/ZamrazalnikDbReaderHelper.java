package radmit.pl.zamrazalnik.domain;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import radmit.pl.zamrazalnik.domain.bo.Miejsce;
import radmit.pl.zamrazalnik.domain.bo.Miejsce2;
import radmit.pl.zamrazalnik.domain.bo.Produkt;
import radmit.pl.zamrazalnik.domain.bo.ZapasyProdukty;
import radmit.pl.zamrazalnik.rest.OsaRestClient;

import org.json.*;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.*;

/**
 * Created by rmorawski on 31.08.16.
 */
public class ZamrazalnikDbReaderHelper  extends SQLiteOpenHelper {
    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 4;
    public static final String DATABASE_NAME = "Zamrazalnik.db";



    public ZamrazalnikDbReaderHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public ArrayList<String> getAllProductsWithQuantityFromLocation(String locationId, String searchItem)
    {
        ArrayList<String> array_list = new ArrayList<String>();

        if(locationId == null || locationId.isEmpty()){
            return array_list;
        }

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from " + ZapasyProdukty.TABLE + " zl inner join " + Produkt.TABLE + " p on zl.PRODUKT_ID = p.ID " +
                " where zl.MIEJSCE_ID=" + locationId +
                " and p.NAZWA_PRODUKT LIKE('%" + searchItem + "%')", null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            array_list.add(
                    res.getString(res.getColumnIndex(Produkt.COLUMN_PRODUCT))
                            + " ( "
                            + res.getInt(res.getColumnIndex(ZapasyProdukty.COLUMN_QUANTITY))
                            + " )"
            );
            res.moveToNext();
        }
        return array_list;
    }

    public ArrayList<String> getAllProductsWithQuantityFromLocation(String locationId)
    {
       return getAllProductsWithQuantityFromLocation(locationId, "");
    }

//    public ArrayList<String> getAllProductsListString()
//    {
//        ArrayList<String> array_list = new ArrayList<String>();
//
//        //hp = new HashMap();
//        SQLiteDatabase db = this.getReadableDatabase();
//        Cursor res =  db.rawQuery( "select * from " + ZapasyProdukty.TABLE, null );
//        res.moveToFirst();
//
//        while(res.isAfterLast() == false){
//            array_list.add(res.getString(res.getColumnIndex(ZapasyProdukty.COLUMN_NAME_PRODUCT)));
//            res.moveToNext();
//        }
//        return array_list;
//    }

    public ArrayList<Produkt> getAllProducts()
    {
        ArrayList<Produkt> array_list = new ArrayList<Produkt>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from " + Produkt.TABLE, null );
        res.moveToFirst();

        while(res.isAfterLast() == false){

            Produkt produkt = new Produkt(
                    res.getLong(res.getColumnIndex(Produkt.COLUMN_ID)),
                    res.getString(res.getColumnIndex(Produkt.COLUMN_PRODUCT))
            );
            array_list.add(produkt);
            res.moveToNext();
        }
        return array_list;
    }

    public ArrayList<Miejsce> getAllLocations()
    {
        final ArrayList<Miejsce> array_list = new ArrayList<Miejsce>();

//        SQLiteDatabase db = this.getReadableDatabase();
//        Cursor res =  db.rawQuery( "select * from " + Miejsce.TABLE, null );
//        res.moveToFirst();
//
//        while(res.isAfterLast() == false){
//
//            Miejsce location = new Miejsce(
//                    res.getLong(res.getColumnIndex(Miejsce.COLUMN_ID)),
//                    res.getString(res.getColumnIndex(Miejsce.COLUMN_MIEJSCE))
//            );
//            array_list.add(location);
//            res.moveToNext();
//        }



        OsaRestClient.get("/place", null, new JsonHttpResponseHandler() {

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
                     array_list.add(new Miejsce(m.getId(), m.getName()));
            }
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
//            @Override
//            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
//                System.out.println("BLOND");
//            }
//
//            @Override
//            public void onSuccess(int statusCode, Header[] headers, String responseString){
//
//            // Define Response class to correspond to the JSON response returned
////            Miejsce miejsce = gson.fromJson(responseString, Miejsce.class);
////                Type collectionType = new TypeToken<List<Miejsce>>(){}.getType();
////                Gson gson = new GsonBuilder().create();
////                List<Miejsce> imageResults = gson.fromJson(responseString, collectionType);
//
////                Type collectionType = new TypeToken<List<Miejsce>>(){}.getType();
////                List<Miejsce> imageResults  = gson.fromJson( responseString , collectionType);
//
//
//                Type collectionType = new TypeToken<List<Miejsce2>>(){}.getType();
//                GsonBuilder gsonBuilder = new GsonBuilder();
////                gsonBuilder.registerTypeAdapter(collectionType, new MultimediaDeserializer());
//                Gson gson = gsonBuilder.create();
//                Miejsce2 miejsce2 = gson.fromJson(responseString, Miejsce2.class);
//                List<Miejsce2> imageResults  = gson.fromJson( responseString , collectionType);
//
//
//            for(Miejsce2 m : imageResults) {
//                array_list.add(new Miejsce(m.getId(), m.getName()));
//            }
//        }
    });

        return array_list;
    }

    private ZapasyProdukty getRecordZapasyProduktyByProductId(Long productId, Long locationId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from " + ZapasyProdukty.TABLE + " where " + ZapasyProdukty.COLUMN_PRODUCT_ID + "="+productId+" and " + ZapasyProdukty.COLUMN_LOCATION_ID + "=" + locationId + "", null );
        res.moveToFirst();

        if (res.getCount() == 0 || res.isNull(res.getColumnIndex(ZapasyProdukty.COLUMN_QUANTITY))) {
            return null;
        } else {
            Produkt produkt = new Produkt(res.getLong(res.getColumnIndex(ZapasyProdukty.COLUMN_PRODUCT_ID)), null);
            Miejsce miejsce = new Miejsce(res.getLong(res.getColumnIndex(ZapasyProdukty.COLUMN_LOCATION_ID)), null);
            ZapasyProdukty zl = new ZapasyProdukty(
                    res.getLong(res.getColumnIndex(ZapasyProdukty.COLUMN_ID)),
                    res.getInt(res.getColumnIndex(ZapasyProdukty.COLUMN_QUANTITY)),
                    produkt,
                    miejsce
                    );
            return zl;
        }
    }

    public boolean insertProductToFridge(Long productId, Long locationId, Integer quantity, String description)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ZapasyProdukty zapasLodowka = getRecordZapasyProduktyByProductId(productId, locationId);

        if (zapasLodowka == null) {
            // insert:
            ContentValues contentValues = new ContentValues();
            contentValues.put(ZapasyProdukty.COLUMN_PRODUCT_ID, productId);
            contentValues.put(ZapasyProdukty.COLUMN_LOCATION_ID, locationId);
            contentValues.put(ZapasyProdukty.COLUMN_QUANTITY, quantity);
            contentValues.put(ZapasyProdukty.COLUMN_DESCRIPTION, description);
            db.insert(ZapasyProdukty.TABLE, null, contentValues);
        } else {
            // modify
            ContentValues contentValues = new ContentValues();
            contentValues.put(ZapasyProdukty.COLUMN_QUANTITY, quantity + zapasLodowka.getQuantity());
            db.update(ZapasyProdukty.TABLE, contentValues, "id = ? ", new String[] { Long.toString(zapasLodowka.getId()) } );
        }

        return true;
    }

    public boolean insertNewProduct (Produkt product)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Produkt.COLUMN_PRODUCT, product.getName());
        db.insert(Produkt.TABLE, null, contentValues);
        return true;
    }

//    public boolean insertNewLocation (Miejsce location)
//    {
//        SQLiteDatabase db = this.getWritableDatabase();
//        ContentValues contentValues = new ContentValues();
//        contentValues.put(Miejsce.COLUMN_MIEJSCE, location.getLocationName());
//        db.insert(Miejsce.TABLE, null, contentValues);
//        return true;
//    }
//public boolean insertNewLocation (Miejsce location)
//{
//
////    JSONObject params = new JSONObject();
////    params.put("name", location.getLocationName());
////    params.put("level", "SECOND");
////    StringEntity entity = new StringEntity(params.toString());
////    entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
////    client.post(context, "http://localhost:8080/searcher/place/", entity, "application/json", responseHandler);
//
//
//    RequestParams params = new RequestParams();
//    params.put("name", location.getLocationName());
//    params.put("level", location.getLevelEnumLocation().name());
//    params.setUseJsonStreamer(true);
//
//    OsaRestClient.post("/item", params, new AsyncHttpResponseHandler() {
//        @Override
//        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
////            Toast.makeText(getApplicationContext(), "Exception: " + e.getMessage(), Toast.LENGTH_SHORT).show();
//            System.out.println("JEST OK, DZIALA");
//        }
//
//        @Override
//        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
//            System.out.println("BLOND");
//        }
//    });
//
//    return true;
//}

    public boolean takeProductFromFridge(String productId, String quantity, String locationId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ZapasyProdukty zapasLodowka = getRecordZapasyProduktyByProductId(Long.valueOf(productId), Long.valueOf(locationId));

        if (zapasLodowka.getQuantity() > 1) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(ZapasyProdukty.COLUMN_QUANTITY, zapasLodowka.getQuantity() - Integer.valueOf(quantity));
            db.update(ZapasyProdukty.TABLE, contentValues, "id = ? ", new String[] { Long.toString(zapasLodowka.getId()) } );
        } else {
            // delete
            db.delete(ZapasyProdukty.TABLE, "id = ? ", new String[] { Long.toString(zapasLodowka.getId()) });
        }

        return true;
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Miejsce.SQL_CREATE_ENTRIES);
        db.execSQL(ZapasyProdukty.SQL_CREATE_ENTRIES);
        db.execSQL(Produkt.SQL_CREATE_ENTRIES);
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(ZapasyProdukty.SQL_DELETE_ENTRIES);
        db.execSQL(Produkt.SQL_DELETE_ENTRIES);
        db.execSQL(Miejsce.SQL_DELETE_ENTRIES);
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
