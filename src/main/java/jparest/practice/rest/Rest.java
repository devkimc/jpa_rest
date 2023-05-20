package jparest.practice.rest;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
public class Rest {

    @Id
    @Column(name = "rest_id", nullable = false)
    private String id;

    @Column(nullable = false)
    private String restname;

    @Enumerated(EnumType.STRING)
    private FoodType foodType;

    @Column(nullable = false)
    private double latCdnt;

    @Column(nullable = false)
    private double lngCdnt;
}
