package jparest.practice.group.service;

import jparest.practice.group.dto.CreateGroupRequest;
import jparest.practice.group.dto.CreateGroupResponse;
import jparest.practice.group.dto.GetUserGroupResponse;
import jparest.practice.user.domain.User;

import java.util.List;

public interface GroupService {

    // 그룹 생성
    CreateGroupResponse createGroup(User user, CreateGroupRequest createGroupRequest);

    // 그룹 탈퇴
    Boolean withdrawGroup(User user, Long groupId);

    // 가입한 그룹 조회
    List<GetUserGroupResponse> getUserGroupList(User user);
}
