package jparest.practice.group.controller;

import jparest.practice.auth.security.CurrentUser;
import jparest.practice.common.util.ApiResult;
import jparest.practice.common.util.ApiUtils;
import jparest.practice.group.dto.*;
import jparest.practice.group.service.GroupService;
import jparest.practice.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/groups")
public class GroupController {
    private final GroupService groupService;

    @PostMapping
    public ApiResult<CreateGroupResponse> createGroup(@CurrentUser User user,
                                                      @Valid @RequestBody CreateGroupRequest createGroupRequest) {
        return ApiUtils.success(groupService.createGroup(user, createGroupRequest));
    }

    @DeleteMapping("/{groupId}/users")
    public ApiResult<Boolean> withdrawGroup(@CurrentUser User user, @PathVariable Long groupId) {
        return ApiUtils.success(groupService.withdrawGroup(user, groupId));
    }

    @GetMapping
    public ApiResult<List<GetGroupUserResponse>> getGroupUserList(@CurrentUser User user) {
        return ApiUtils.success(groupService.getGroupUserList(user));
    }

    @PatchMapping("/{groupId}/owners")
    public ApiResult<ChangeOwnerResponse> changeOwner(@CurrentUser User user,
                                                      @PathVariable Long groupId,
                                                      ChangeOwnerRequest changeOwnerRequest) {
        return ApiUtils.success(groupService.changeOwner(user, groupId, changeOwnerRequest));
    }
}
