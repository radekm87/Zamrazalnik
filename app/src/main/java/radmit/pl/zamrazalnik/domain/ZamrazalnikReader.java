package radmit.pl.zamrazalnik.domain;

import android.provider.BaseColumns;

/**
 * Created by rmorawski on 31.08.16.
 */
public final class ZamrazalnikReader {
    // To prevent someone from accidentally instantiating the contract class,
    // make the constructor private.
    private ZamrazalnikReader() {}

    private static final String TEXT_TYPE = " TEXT";
    private static final String INTEGER_TYPE = " INTEGER";
    private static final String COMMA_SEP = ",";
    public static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + FeedEntry.TABLE_NAME + " (" +
                    FeedEntry._ID + " INTEGER PRIMARY KEY," +
                    FeedEntry.COLUMN_NAME_PRODUCT + INTEGER_TYPE + COMMA_SEP +
                    FeedEntry.COLUMN_NAME_QUANTITY + INTEGER_TYPE + " )";

    public static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + FeedEntry.TABLE_NAME;



    /* Inner class that defines the table contents */
    static class FeedEntry implements BaseColumns {
        public static final String TABLE_NAME = "ZAPASY_LODOWKA";

        public static final String COLUMN_NAME_PRODUCT = "PRODUKT_ID";
        public static final String COLUMN_NAME_QUANTITY = "ILOSC";
    }
}

