package jparest.practice.rest.domain;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Rest {

    @Id
    @Column(name = "rest_id", nullable = false, length = 16)
    private String id;

    @Column(nullable = false)
    private String restName;

    @Column(nullable = false)
    private double latitude;

    @Column(nullable = false)
    private double longitude;

    @OneToMany(mappedBy = "rest")
    private List<GroupRest> groupRests = new ArrayList<GroupRest>();
}
