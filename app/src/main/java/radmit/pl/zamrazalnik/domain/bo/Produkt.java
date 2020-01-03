package radmit.pl.zamrazalnik.domain.bo;

/**
 * Created by rmorawski on 02.09.16.
 */
public class Produkt {

    public static final String TABLE = "PRODUKT";
    public static final String COLUMN_ID = "ID";
    public static final String COLUMN_PRODUCT = "NAZWA_PRODUKT";

    private static final String TEXT_TYPE = " TEXT";
    private static final String INTEGER_TYPE = " INTEGER";
    private static final String COMMA_SEP = ",";
    public static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + TABLE + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY " + COMMA_SEP +
                    COLUMN_PRODUCT + TEXT_TYPE  +
                    " )";
    public static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + TABLE;

    private Long id;
    private String name;
    private String description;

    public Produkt(String productName, String productDesc) {
        this.name = productName;
        this.description = productDesc;
    }

    public Produkt(Long id, String productName) {
        this.id = id;
        this.name = productName;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
}
