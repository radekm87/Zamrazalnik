package radmit.pl.zamrazalnik.domain;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

import radmit.pl.zamrazalnik.domain.bo.Miejsce;
import radmit.pl.zamrazalnik.domain.bo.Produkt;
import radmit.pl.zamrazalnik.domain.bo.ZapasyProdukty;

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
        ArrayList<Miejsce> array_list = new ArrayList<Miejsce>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from " + Miejsce.TABLE, null );
        res.moveToFirst();

        while(res.isAfterLast() == false){

            Miejsce location = new Miejsce(
                    res.getLong(res.getColumnIndex(Miejsce.COLUMN_ID)),
                    res.getString(res.getColumnIndex(Miejsce.COLUMN_MIEJSCE))
            );
            array_list.add(location);
            res.moveToNext();
        }
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
        contentValues.put(Produkt.COLUMN_PRODUCT, product.getProductName());
        db.insert(Produkt.TABLE, null, contentValues);
        return true;
    }

    public boolean insertNewLocation (Miejsce location)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Miejsce.COLUMN_MIEJSCE, location.getLocationName());
        db.insert(Miejsce.TABLE, null, contentValues);
        return true;
    }

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
