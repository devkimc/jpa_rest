package jparest.practice.invite.service;

import jparest.practice.group.domain.Group;
import jparest.practice.invite.domain.Invite;
import jparest.practice.user.domain.User;

import java.util.UUID;

public interface InviteService {

    // 그룹으로 초대
    Invite inviteToGroup(Long groupId, User sendUser, UUID recvUserId);

    // 초대 수락
    Group agreeInvitation(Long inviteId, User recvUser);

    // 초대 거절
    boolean rejectInvitation(Long inviteId, User recvUser);

    // 초대 취소
    boolean cancelInvitation(Long inviteId, User sendUser);
}
