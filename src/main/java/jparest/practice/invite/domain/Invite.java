package jparest.practice.invite.domain;

import jparest.practice.group.domain.GroupMember;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class Invite {

    @Id
    @GeneratedValue
    @Column(name = "invite_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_member_id")
    private GroupMember groupMember;

    private Long recvUserId;

    @Enumerated(EnumType.STRING)
    private InviteStatus status;

    //==생성 메서드==//
    public static Invite createInvite(Long recvUserId, GroupMember groupMember) {
        Invite invite = new Invite();
        invite.setRecvUserId(recvUserId);
        invite.setGroupMember(groupMember);
        invite.setStatus(InviteStatus.WAITING);

        return invite;
    }
}
