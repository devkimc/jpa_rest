package jparest.practice.group.domain;

import jparest.practice.common.util.TimeBaseEntity;
import jparest.practice.rest.domain.GroupRest;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Entity
@Table(name = "groups")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Group extends TimeBaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "group_id")
    private Long id;

    @Column(nullable = false, length = 20)
    private String groupName;

    @Builder.Default
    @OneToMany(mappedBy = "group", orphanRemoval = true)
    private List<GroupUser> groupUsers = new ArrayList<GroupUser>();

    @Builder.Default
    @OneToMany(mappedBy = "group", orphanRemoval = true)
    private List<GroupRest> groupRests = new ArrayList<GroupRest>();

    @Column(nullable = false)
    private Boolean isPublic;

    public Group(String groupName) {
        this.groupName = groupName;
    }

    public int getUserCount() {
        return this.getGroupUsers().size();
    }

    public int getRestCount() {
        return this.getGroupRests().size();
    }

    public boolean isJoinUser(UUID userId) {
        long matchUserCount = this.getGroupUsers()
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
