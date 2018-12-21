package radmit.pl.zamrazalnik.domain.bo;

import android.provider.BaseColumns;

/**
 * Created by rmorawski on 31.08.16.
 */
public class ZapasyProdukty {
    public static final String TABLE = "ZAPASY_PRODUKTY";

    public static final String COLUMN_ID = "ID";
    public static final String COLUMN_PRODUCT_ID = "PRODUKT_ID";
    public static final String COLUMN_LOCATION_ID = "MIEJSCE_ID";
    public static final String COLUMN_QUANTITY = "ILOSC";
    public static final String COLUMN_DESCRIPTION = "OPIS";

    private static final String TEXT_TYPE = " TEXT";
    private static final String INTEGER_TYPE = " INTEGER";
    private static final String COMMA_SEP = ",";
    public static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + TABLE + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY," +
                    COLUMN_PRODUCT_ID + INTEGER_TYPE + COMMA_SEP +
                    COLUMN_LOCATION_ID + INTEGER_TYPE + COMMA_SEP +
                    COLUMN_QUANTITY + INTEGER_TYPE + COMMA_SEP +
                    COLUMN_DESCRIPTION + TEXT_TYPE + " )";
    public static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + TABLE;


    private Long id;
    private Produkt productDbVo;
    private Miejsce locationDbVo;
    private Integer quantity;

    public ZapasyProdukty(Produkt productDbVo, Miejsce locationDbVo, Integer quantity) {
        this.productDbVo = productDbVo;
        this.quantity = quantity;
        this.locationDbVo = locationDbVo;
    }
    public ZapasyProdukty(Long id, Integer quantity, Produkt productDbVo, Miejsce locDbVo) {
        this.id = id;
        this.quantity = quantity;
        this.productDbVo = productDbVo;
        this.locationDbVo = locDbVo;
    }

    public Long getId() {
        return id;
    }

    public Produkt getProductDbVo() {
        return productDbVo;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public Miejsce getLocationDbVo() {
        return locationDbVo;
    }
}

