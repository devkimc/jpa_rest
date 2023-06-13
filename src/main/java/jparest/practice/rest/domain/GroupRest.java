package jparest.practice.rest.domain;

import jparest.practice.common.util.TimeBaseEntity;
import jparest.practice.group.domain.Group;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class GroupRest extends TimeBaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "group_rest_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "group_id")
    private Group group;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "rest_id")
    private Rest rest;

    public GroupRest(Group group, Rest rest) {
        this.group = group;
        this.rest = rest;
    }

    //==생성 메서드==//
    public static GroupRest createGroupRest(Group group, Rest rest) {
        GroupRest groupRest = new GroupRest(group, rest);
//        addGroupRest(rest);
        group.getGroupRests().add(groupRest);
        return groupRest;
    }

    //==연관관계 편의 메서드==//
    public void addGroupRest(Rest rest) {
        this.rest = rest;
        group.getGroupRests().add(this);
    }
}
