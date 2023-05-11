package jparest.practice.group.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "groups")
@Getter
@Setter
public class Group {

    @Id
    @GeneratedValue
    @Column(name = "group_id")
    private Long id;

    private Long createUserId;
    private String groupName;

    @OneToMany(mappedBy = "group")
    private List<GroupUser> groupUsers = new ArrayList<>();

    //==연관관계 메서드==//
    public void addGroupUser(GroupUser groupUser) {
        groupUsers.add(groupUser);
        groupUser.setGroup(this);
    }

    //==생성 메서드==//
    public static Group createGroup(Long createUserId, String groupName,
                                    GroupUser groupUser) {
        Group group = new Group();
        group.setCreateUserId(createUserId);
        group.setGroupName(groupName);

        group.addGroupUser(groupUser);
        return group;
    }
}
