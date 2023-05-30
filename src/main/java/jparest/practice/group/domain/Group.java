package jparest.practice.group.domain;

import jparest.practice.rest.domain.GroupRest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Entity
@Table(name = "groups")
@Getter
public class Group {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "group_id")
    private Long id;

    @Column(nullable = false)
    private String groupName;

    @OneToMany(mappedBy = "group")
    private List<UserGroup> userGroups = new ArrayList<UserGroup>();

    @OneToMany(mappedBy = "group")
    private List<GroupRest> groupRests = new ArrayList<GroupRest>();

    public Group(String groupName) {
        this.groupName = groupName;
    }

    public int getUserCount() {
        return this.getUserGroups().size();
    }

    public int getRestCount() {
        return this.getGroupRests().size();
    }

    public boolean isJoinUser(UUID userId) {
        long matchUserCount = this.getUserGroups()
                .stream()
                .filter(e -> e.getUser().getId().equals(userId))
                .count();

        if (matchUserCount == 1) {
            return true;
        }

        return false;
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
