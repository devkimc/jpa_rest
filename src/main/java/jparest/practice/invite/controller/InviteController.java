package jparest.practice.invite.controller;

import jparest.practice.auth.security.CurrentUser;
import jparest.practice.common.util.ApiResult;
import jparest.practice.common.util.ApiUtils;
import jparest.practice.group.domain.Group;
import jparest.practice.group.service.GroupService;
import jparest.practice.invite.service.InviteService;
import jparest.practice.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/invite")
public class InviteController {

    private final GroupService groupService;
    private final InviteService inviteService;

    @PatchMapping(value = "/{inviteId}/agree")
    public ApiResult<Boolean> agreeInvite(@CurrentUser User user,
                                          @PathVariable Long inviteId
    ) {

        Group invitedGroup = inviteService.agreeInvitation(inviteId, user);
        groupService.addUserGroup(user, invitedGroup.getId());

        return ApiUtils.success(Boolean.TRUE);
    }
}
