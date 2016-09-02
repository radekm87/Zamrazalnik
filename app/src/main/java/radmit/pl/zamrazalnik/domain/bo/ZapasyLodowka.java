package radmit.pl.zamrazalnik.domain.bo;

import android.provider.BaseColumns;

/**
 * Created by rmorawski on 31.08.16.
 */
public class ZapasyLodowka {
    public static final String TABLE = "ZAPASY_LODOWKA";

    public static final String COLUMN_ID = "ID";
    public static final String COLUMN_PRODUCT_ID = "PRODUKT_ID";
    public static final String COLUMN_QUANTITY = "ILOSC";

    private static final String TEXT_TYPE = " TEXT";
    private static final String INTEGER_TYPE = " INTEGER";
    private static final String COMMA_SEP = ",";
    public static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + TABLE + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY," +
                    COLUMN_PRODUCT_ID + INTEGER_TYPE + COMMA_SEP +
                    COLUMN_QUANTITY + INTEGER_TYPE + " )";
    public static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + TABLE;


    private Long id;
    private Produkt productDbVo;
    private Integer quantity;

    public ZapasyLodowka(Produkt productDbVo, Integer quantity) {
        this.productDbVo = productDbVo;
        this.quantity = quantity;
    }
    public ZapasyLodowka(Long id, Integer quantity, Produkt productDbVo) {
        this.id = id;
        this.quantity = quantity;
        this.productDbVo = productDbVo;
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

}

