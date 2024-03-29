package jparest.practice.invite.controller;

import jparest.practice.auth.security.CurrentUser;
import jparest.practice.common.util.ApiResult;
import jparest.practice.common.util.ApiUtils;
import jparest.practice.invite.dto.GetWaitingInviteResponse;
import jparest.practice.invite.dto.ProcessInviteRequest;
import jparest.practice.invite.dto.InviteUserRequest;
import jparest.practice.invite.dto.InviteUserResponse;
import jparest.practice.invite.service.InviteService;
import jparest.practice.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/groups/invites")
public class InviteController {

    private final InviteService inviteService;

    @GetMapping
    public ApiResult<List<GetWaitingInviteResponse>> getInvitedList(@CurrentUser User user) {
        return ApiUtils.success(inviteService.getWaitingInviteList(user));
    }

    @PostMapping
    public ApiResult<InviteUserResponse> inviteUser(@CurrentUser User user, @Valid InviteUserRequest inviteUserRequest) {
        return ApiUtils.success(inviteService.inviteToGroup(user, inviteUserRequest));
    }

    @PatchMapping(value = "/{inviteId}/status")
    public ApiResult<Boolean> processInvite(@CurrentUser User user,
                                         @PathVariable Long inviteId,
                                         @RequestBody ProcessInviteRequest processInviteRequest
    ) {
        return ApiUtils.success(inviteService.processInvite(user, inviteId, processInviteRequest));
    }
}
