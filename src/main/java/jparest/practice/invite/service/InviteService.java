package jparest.practice.invite.service;

import jparest.practice.invite.domain.Invite;
import jparest.practice.invite.domain.InviteStatus;
import jparest.practice.invite.dto.InviteStatusPatchRequest;
import jparest.practice.user.domain.User;

import java.util.UUID;

public interface InviteService {

    // 그룹으로 초대
    Invite inviteToGroup(Long groupId, User sendUser, UUID recvUserId);

    // 초대 처리(수락, 거절, 취소 등)
    boolean procInvitation(Long inviteId, User user, InviteStatus requestStatus);
}
