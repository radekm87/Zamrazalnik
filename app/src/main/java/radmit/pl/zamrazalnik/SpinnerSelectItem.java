package radmit.pl.zamrazalnik;

/**
 * Created by rmorawski on 02.09.16.
 */
public class SpinnerSelectItem {
    private Long id;
    private String value;

    public SpinnerSelectItem(Long id, String value) {
        this.id = id;
        this.value = value;
    }

    public Long getId() {
        return id;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }
}
