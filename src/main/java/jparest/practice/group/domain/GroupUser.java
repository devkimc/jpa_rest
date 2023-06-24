package jparest.practice.group.domain;

import jparest.practice.common.util.TimeBaseEntity;
import jparest.practice.invite.domain.Invite;
import jparest.practice.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "group_user")
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GroupUser extends TimeBaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "group_user_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "group_id", nullable = false)
    private Group group;

    @Builder.Default
    @OneToMany(mappedBy = "sendGroupUser", orphanRemoval = true)
    private List<Invite> invites = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private GroupUserType groupUserType;

    public void setGroup(Group group) {
        this.group = group;
    }

    //==연관관계 메서드==//
    public void addGroupUser() {
        this.user.getGroupUsers().add(this);
        this.group.getGroupUsers().add(this);
    }

    //==생성 메서드==//
//    public static UserGroup createGroupUser(Optional<User> user) {
//        UserGroup userGroup = new UserGroup();
//        groupUser.setUser(user);
//        return userGroup;
//    }

}
