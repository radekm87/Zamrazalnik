package radmit.pl.zamrazalnik.domain.bo;

import java.io.Serializable;

public class Miejsce2 implements Serializable {


    private Long id;
    private String name;

    public Miejsce2() {
    }

    public Miejsce2(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
