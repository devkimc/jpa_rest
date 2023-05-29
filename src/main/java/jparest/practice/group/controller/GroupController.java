package jparest.practice.group.controller;

import jparest.practice.auth.security.CurrentUser;
import jparest.practice.common.util.ApiResult;
import jparest.practice.common.util.ApiUtils;
import jparest.practice.group.domain.Group;
import jparest.practice.group.dto.CreateGroupRequest;
import jparest.practice.group.dto.CreateGroupResponse;
import jparest.practice.group.dto.GetUserGroupResponse;
import jparest.practice.group.service.GroupService;
import jparest.practice.group.service.GroupServiceImpl;
import jparest.practice.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class GroupController {
    private final GroupService groupService;

    @PostMapping("/group")
    public ApiResult<CreateGroupResponse> createGroup(@CurrentUser User user, @RequestBody CreateGroupRequest createGroupRequest) {
        return ApiUtils.success(groupService.createGroup(user, createGroupRequest.getGroupName()));
    }

    @DeleteMapping("/group/{groupId}/user")
    public ApiResult<Boolean> withdrawGroup(@CurrentUser User user, @PathVariable Long groupId) {
        return ApiUtils.success(groupService.withdrawGroup(user, groupId));
    }

    @GetMapping("/groups")
    public ApiResult<List<GetUserGroupResponse>> getUserGroupList(@CurrentUser User user) {
        return ApiUtils.success(groupService.getUserGroupList(user));
    }
}
