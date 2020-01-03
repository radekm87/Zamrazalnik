package radmit.pl.zamrazalnik.domain.bo;

public enum LocationEnum {
    FIRST("Główna"),
    SECOND("Podrzędna");

    private String showName;

    LocationEnum(String showName) {
        this.showName = showName;
    }

    public String getShowName() {
        return showName;
    }

    @Override
    public String toString() {
        return showName;
    }
}
