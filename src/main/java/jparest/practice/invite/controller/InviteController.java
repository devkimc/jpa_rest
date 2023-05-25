package jparest.practice.invite.controller;

import jparest.practice.auth.security.CurrentUser;
import jparest.practice.common.util.ApiResult;
import jparest.practice.common.util.ApiUtils;
import jparest.practice.invite.dto.InviteStatusPatchRequest;
import jparest.practice.invite.service.InviteService;
import jparest.practice.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/invite")
public class InviteController {

    private final InviteService inviteService;

    @PatchMapping(value = "/{inviteId}/status")
    public ApiResult<Boolean> procInvite(@CurrentUser User user,
                                         @PathVariable Long inviteId,
                                         @RequestBody InviteStatusPatchRequest inviteStatusPatchRequest
    ) {
        inviteService.procInvitation(inviteId, user, inviteStatusPatchRequest);
        return ApiUtils.success(Boolean.TRUE);
    }

}
