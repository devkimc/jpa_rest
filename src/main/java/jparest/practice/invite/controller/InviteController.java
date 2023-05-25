package jparest.practice.invite.controller;

import jparest.practice.auth.security.CurrentUser;
import jparest.practice.common.util.ApiResult;
import jparest.practice.common.util.ApiUtils;
import jparest.practice.invite.domain.Invite;
import jparest.practice.invite.dto.InviteStatusPatchRequest;
import jparest.practice.invite.dto.InviteUserRequest;
import jparest.practice.invite.dto.InviteUserResponse;
import jparest.practice.invite.service.InviteService;
import jparest.practice.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/invite")
public class InviteController {

    private final InviteService inviteService;

    @PostMapping
    public ApiResult<InviteUserResponse> inviteUser(@CurrentUser User user, InviteUserRequest inviteUserRequest) {
        Invite invite = inviteService.inviteToGroup(inviteUserRequest.getGroupId(), user, inviteUserRequest.getRecvUserId());
        InviteUserResponse response = InviteUserResponse.builder().inviteId(invite.getId()).build();
        return ApiUtils.success(response);
    }


    @PatchMapping(value = "/{inviteId}/status")
    public ApiResult<Boolean> procInvite(@CurrentUser User user,
                                         @PathVariable Long inviteId,
                                         @RequestBody InviteStatusPatchRequest inviteStatusPatchRequest
    ) {
        return ApiUtils.success(inviteService.procInvitation(inviteId, user, inviteStatusPatchRequest.getInviteStatus()));
    }
}
