package jparest.practice.group.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "groups")
@Getter
@AllArgsConstructor
public class Group {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "group_id")
    private Long id;

    @Column(nullable = false)
    private String groupName;

    @OneToMany(mappedBy = "group")
    private List<UserGroup> userGroups = new ArrayList<>();

    //==연관관계 메서드==//
    public void addUserGroup(UserGroup userGroup) {
        userGroups.add(userGroup);
        userGroup.setGroup(this);
    }

    //==생성 메서드==//
//    public static Group createGroup(String groupName,
//                                    GroupUser groupUser) {
//        Group group = new Group();
//        group.setGroupName(groupName);
//
//        group.addGroupUser(groupUser);
//        return group;
//    }
}
