package jparest.practice.group.domain;

import jparest.practice.invite.domain.Invite;
import jparest.practice.user.domain.User;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "group_member")
@Getter
@Setter
public class GroupMember {

    @Id
    @GeneratedValue
    @Column(name = "group_member_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id")
    private Group group;

    @OneToMany(mappedBy = "groupMember")
    private List<Invite> invites = new ArrayList<>();

    //==연관관계 메서드==//
    public void addInvite(Invite invite) {
        invites.add(invite);
        invite.setGroupMember(this);
    }

    //==생성 메서드==//
    public static GroupMember createGroupMember(User user) {
        GroupMember groupMember = new GroupMember();
        groupMember.setUser(user);

        return groupMember;
    }

}
