package jparest.practice.rest.domain;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
public class Rest {

    @Id
    @Column(name = "rest_id", nullable = false, length = 16)
    private String id;

    @Column(nullable = false)
    private String restname;

    @Column(nullable = false)
    private double latitude;

    @Column(nullable = false)
    private double longitude;

    // TODO : 왜 매핑이 안될까?
//    @OneToMany(mappedBy = "rest")
//    private List<Rest> rests = new ArrayList<Rest>();
}
