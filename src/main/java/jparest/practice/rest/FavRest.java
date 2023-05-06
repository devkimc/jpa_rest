package jparest.practice.rest;

import jparest.practice.group.domain.Group;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class FavRest {

    @Id
    @GeneratedValue
    @Column(name = "fav_rest_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id")
    private Group group;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rest_id")
    private Rest rest;
}
