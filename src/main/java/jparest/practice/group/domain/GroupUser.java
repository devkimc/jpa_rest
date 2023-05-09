package jparest.practice.group.domain;

import jparest.practice.invite.domain.Invite;
import jparest.practice.user.domain.User;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "group_user")
@Getter
@Setter
public class GroupUser {

    @Id
    @GeneratedValue
    @Column(name = "group_user_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id")
    private Group group;

    @OneToMany(mappedBy = "groupUser")
    private List<Invite> invites = new ArrayList<>();

    //==연관관계 메서드==//
    public void addInvite(Invite invite) {
        invites.add(invite);
        invite.setGroupUser(this);
    }

    //==생성 메서드==//
    public static GroupUser createGroupUser(User user) {
        GroupUser groupUser = new GroupUser();
        groupUser.setUser(user);

        return groupUser;
    }

}
