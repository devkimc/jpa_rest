package jparest.practice.invite.domain;

import jparest.practice.group.domain.Group;
import jparest.practice.group.domain.UserGroup;
import jparest.practice.user.domain.User;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Getter
@Setter
public class Invite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "invite_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_group_id", nullable = false)
    private UserGroup sendUserGroup;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User recvUser;

    @Enumerated(EnumType.STRING)
    private InviteStatus inviteStatus;

    //==생성 메서드==//
//    public static Invite createInvite(Long recvUserId, UserGroup userGroup) {
//        Invite invite = new Invite();
//        invite.setRecvUserId(recvUserId);
//        invite.setUserGroup(userGroup);
//        invite.setStatus(InviteStatus.WAITING);
//
//        return invite;
//    }
}
