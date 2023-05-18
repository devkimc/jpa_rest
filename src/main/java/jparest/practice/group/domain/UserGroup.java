package jparest.practice.group.domain;

import jparest.practice.invite.domain.Invite;
import jparest.practice.user.domain.User;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Entity
@Table(name = "user_group")
@Getter
@Setter
public class UserGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_group_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id", nullable = false)
    private Group group;

    //==연관관계 메서드==//
//    public void addInvite(Invite invite) {
//        invites.add(invite);
//        invite.setUserGroup(this);
//    }

    //==생성 메서드==//
    public static UserGroup createGroupUser(Optional<User> user) {
        UserGroup userGroup = new UserGroup();
//        groupUser.setUser(user);

        return userGroup;
    }

}