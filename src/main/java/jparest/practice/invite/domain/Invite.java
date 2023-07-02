package jparest.practice.invite.domain;

import jparest.practice.common.util.TimeBaseEntity;
import jparest.practice.group.domain.GroupUser;
import jparest.practice.invite.exception.AlreadyProcessedInviteException;
import jparest.practice.invite.exception.InviteNotFoundException;
import jparest.practice.user.domain.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static jparest.practice.invite.domain.InviteStatus.*;

@Table(name = "group_invite")
@Entity
@Getter
@NoArgsConstructor
public class Invite extends TimeBaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "invite_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "group_user_id")
    private GroupUser sendGroupUser;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id")
    private User recvUser;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private InviteStatus inviteStatus;

    public void setRecvUser(User user) {
        this.recvUser = user;
    }

    public void updateStatus(InviteStatus inviteStatus) {
        this.inviteStatus = inviteStatus;
    }

    public Invite(GroupUser sendGroupUser, User recvUser, InviteStatus inviteStatus) {
        this.sendGroupUser = sendGroupUser;
        this.recvUser = recvUser;
        this.inviteStatus = inviteStatus;
    }

    //==생성 메서드==//
    public static Invite createInvite(GroupUser sendGroupUser, User recvUser) {
        Invite invite = new Invite(sendGroupUser, recvUser, WAITING);
        sendGroupUser.getInvites().add(invite);
        return invite;
    }

    // 초대 처리에 대한 권한 체크
    public void chkAuthorizationOfInvitation(User user, InviteStatus requestStatus) {

        if(requestStatus == ACCEPT && !this.getRecvUser().equals(user)) {
            throw new InviteNotFoundException("승낙 요청한 유저의 초대가 아닙니다.");
        }

        if (requestStatus == ACCEPT && this.getInviteStatus() != WAITING) {
            throw new AlreadyProcessedInviteException("inviteId = " + this.getId());
        }

        if(requestStatus == REJECT && !this.getRecvUser().equals(user)) {
            throw new InviteNotFoundException("거절 요청한 유저의 초대가 아닙니다.");
        }

        if(requestStatus == CANCEL && !this.getSendGroupUser().getUser().equals(user)) {
            throw new InviteNotFoundException("취소 요청한 유저의 초대가 아닙니다.");
        }
    }
}
