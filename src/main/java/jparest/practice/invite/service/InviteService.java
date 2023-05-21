package jparest.practice.invite.service;

import java.util.UUID;

public interface InviteService {

    // 그룹으로 초대
    boolean inviteToGroup(Long groupId, UUID sendUserId, UUID recvUserId);

    // 초대 수락
    boolean agreeInvitation(Long inviteId, UUID recvUserId);

    // 초대 거절
    boolean rejectInvitation(Long inviteId, UUID recvUserId);

    // 초대 취소
    boolean cancelInvitation(Long inviteId, UUID recvUserId);
}
