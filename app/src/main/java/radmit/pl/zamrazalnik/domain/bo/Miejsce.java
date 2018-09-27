package radmit.pl.zamrazalnik.domain.bo;

import java.io.Serializable;

public class Miejsce implements Serializable {

    public static final String TABLE = "MIEJSCE";
    public static final String COLUMN_ID = "ID";
    public static final String COLUMN_MIEJSCE = "NAZWA_MIEJSCA";

    private static final String TEXT_TYPE = " TEXT";
    private static final String INTEGER_TYPE = " INTEGER";
    private static final String COMMA_SEP = ",";
    public static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + TABLE + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY " + COMMA_SEP +
                    COLUMN_MIEJSCE + TEXT_TYPE  +
                    " )";
    public static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + TABLE;

    private Long id;
    private String locationName;

    public Miejsce(String locationName) {
        this.locationName = locationName;
    }

    public Miejsce(Long id, String locationName) {
        this.id = id;
        this.locationName = locationName;
    }

    public Long getId() {
        return id;
    }

    public String getLocationName() {
        return locationName;
    }


    @Override
    public String toString() {
        return this.locationName;
    }
}
