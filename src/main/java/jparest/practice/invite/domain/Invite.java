package jparest.practice.invite.domain;

import jparest.practice.group.domain.GroupUser;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class Invite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "invite_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_user_id")
    private GroupUser groupUser;

    private Long recvUserId;

    @Enumerated(EnumType.STRING)
    private InviteStatus status;

    //==생성 메서드==//
    public static Invite createInvite(Long recvUserId, GroupUser groupUser) {
        Invite invite = new Invite();
        invite.setRecvUserId(recvUserId);
        invite.setGroupUser(groupUser);
        invite.setStatus(InviteStatus.WAITING);

        return invite;
    }
}
