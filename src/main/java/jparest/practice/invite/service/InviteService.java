package jparest.practice.invite.service;

import jparest.practice.invite.dto.GetWaitingInviteResponse;
import jparest.practice.invite.dto.InviteUserRequest;
import jparest.practice.invite.dto.InviteUserResponse;
import jparest.practice.invite.dto.ProcessInviteRequest;
import jparest.practice.user.domain.User;

import java.util.List;

public interface InviteService {

    // 그룹으로 초대
    InviteUserResponse inviteToGroup(User sendUser, InviteUserRequest inviteUserRequest);

    // 초대 처리(수락, 거절, 취소 등)
    boolean processInvite(User user, Long inviteId, ProcessInviteRequest processInviteRequest);

    // 대기중인 초대 리스트 조회
    List<GetWaitingInviteResponse> getWaitingInviteList(User user);

}
