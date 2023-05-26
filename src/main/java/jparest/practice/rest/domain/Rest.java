package jparest.practice.rest.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
public class Rest {

    @Id
    @Column(name = "rest_id", nullable = false, length = 16)
    private String id;

    @Column(nullable = false)
    private String restname;

    @Column(nullable = false)
    private double latCdnt;

    @Column(nullable = false)
    private double lngCdnt;
}
