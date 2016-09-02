package radmit.pl.zamrazalnik.domain;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

import radmit.pl.zamrazalnik.domain.bo.Produkt;
import radmit.pl.zamrazalnik.domain.bo.ZapasyLodowka;

/**
 * Created by rmorawski on 31.08.16.
 */
public class ZamrazalnikDbReaderHelper  extends SQLiteOpenHelper {
    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 2;
    public static final String DATABASE_NAME = "Zamrazalnik.db";

    public ZamrazalnikDbReaderHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public ArrayList<String> getAllProductsWithQuantity()
    {
        ArrayList<String> array_list = new ArrayList<String>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from " + ZapasyLodowka.TABLE + " zl inner join " + Produkt.TABLE + " p on zl.PRODUKT_ID = p.ID", null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            array_list.add(
                    res.getString(res.getColumnIndex(Produkt.COLUMN_PRODUCT))
                    + " ( "
                    + res.getInt(res.getColumnIndex(ZapasyLodowka.COLUMN_QUANTITY))
                    + " )"
            );
            res.moveToNext();
        }
        return array_list;
    }

//    public ArrayList<String> getAllProductsListString()
//    {
//        ArrayList<String> array_list = new ArrayList<String>();
//
//        //hp = new HashMap();
//        SQLiteDatabase db = this.getReadableDatabase();
//        Cursor res =  db.rawQuery( "select * from " + ZapasyLodowka.TABLE, null );
//        res.moveToFirst();
//
//        while(res.isAfterLast() == false){
//            array_list.add(res.getString(res.getColumnIndex(ZapasyLodowka.COLUMN_NAME_PRODUCT)));
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

    private ZapasyLodowka getRecordZapasyLodowkaByProductId(Long productId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from " + ZapasyLodowka.TABLE + " where " + ZapasyLodowka.COLUMN_PRODUCT_ID + "="+productId+"", null );
        res.moveToFirst();

        if (res.getCount() == 0 || res.isNull(res.getColumnIndex(ZapasyLodowka.COLUMN_QUANTITY))) {
            return null;
        } else {
            Produkt produkt = new Produkt(res.getLong(res.getColumnIndex(ZapasyLodowka.COLUMN_PRODUCT_ID)), null);
            ZapasyLodowka zl = new ZapasyLodowka(
                    res.getLong(res.getColumnIndex(ZapasyLodowka.COLUMN_ID)),
                    res.getInt(res.getColumnIndex(ZapasyLodowka.COLUMN_QUANTITY)),
                    produkt
                    );
            return zl;
        }
    }

    public boolean insertProductToFridge(Long productId, Integer quantity)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ZapasyLodowka zapasLodowka = getRecordZapasyLodowkaByProductId(productId);

        if (zapasLodowka == null) {
            // insert:
            ContentValues contentValues = new ContentValues();
            contentValues.put(ZapasyLodowka.COLUMN_PRODUCT_ID, productId);
            contentValues.put(ZapasyLodowka.COLUMN_QUANTITY, quantity);
            db.insert(ZapasyLodowka.TABLE, null, contentValues);
        } else {
            // modify
            ContentValues contentValues = new ContentValues();
            contentValues.put(ZapasyLodowka.COLUMN_QUANTITY, quantity + zapasLodowka.getQuantity());
            db.update(ZapasyLodowka.TABLE, contentValues, "id = ? ", new String[] { Long.toString(zapasLodowka.getId()) } );
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

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(ZapasyLodowka.SQL_CREATE_ENTRIES);
        db.execSQL(Produkt.SQL_CREATE_ENTRIES);
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(ZapasyLodowka.SQL_DELETE_ENTRIES);
        db.execSQL(Produkt.SQL_DELETE_ENTRIES);
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
