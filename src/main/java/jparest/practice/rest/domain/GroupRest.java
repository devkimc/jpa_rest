package jparest.practice.rest.domain;

import jparest.practice.group.domain.Group;
import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
public class GroupRest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "group_rest_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id")
    private Group group;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rest_id")
    private Rest rest;

    //==연관관계 편의 메서드==//
    public void addGroupRest(Rest rest) {
//        this.rest = rest;
//        group.getGroupRests()
    }
}
