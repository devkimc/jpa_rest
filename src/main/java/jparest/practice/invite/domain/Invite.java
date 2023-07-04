package jparest.practice.invite.domain;

import jparest.practice.common.util.TimeBaseEntity;
import jparest.practice.group.domain.GroupUser;
import jparest.practice.invite.exception.InviteNotFoundException;
import jparest.practice.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static jparest.practice.invite.domain.InviteStatus.*;

@Table(name = "group_invite")
@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
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
    private InviteStatus status;

    public void updateStatus(InviteStatus status) {
        this.status = status;
    }

    public Invite(GroupUser sendGroupUser, User recvUser, InviteStatus status) {
        this.sendGroupUser = sendGroupUser;
        this.recvUser = recvUser;
        this.status = status;
    }

    //==생성 메서드==//
    public static Invite createInvite(GroupUser sendGroupUser, User recvUser) {
        Invite invite = Invite.builder()
                .sendGroupUser(sendGroupUser)
                .recvUser(recvUser)
                .status(WAITING)
                .build();

        sendGroupUser.getInvites().add(invite);
        return invite;
    }

    // 초대 처리에 대한 권한 체크
    public void chkAuthorizationOfInviteProcess(User user, InviteStatus status) {

        String strInviteIdAndUserId = "inviteId = " + this.id + ", userId = " + user.getId();

        if (status == ACCEPT && !user.equals(this.getRecvUser())) {
            throw new InviteNotFoundException("승낙 요청한 유저의 초대가 아닙니다." + strInviteIdAndUserId);
        }

        if (status == REJECT && !user.equals(this.getRecvUser())) {
            throw new InviteNotFoundException("거절 요청한 유저의 초대가 아닙니다." + strInviteIdAndUserId);
        }

        if (status == CANCEL && !user.equals(this.getSendGroupUser().getUser())) {
            throw new InviteNotFoundException("취소 요청한 유저의 초대가 아닙니다." + strInviteIdAndUserId);
        }
    }
}
