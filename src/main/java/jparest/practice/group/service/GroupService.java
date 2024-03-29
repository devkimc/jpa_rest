package jparest.practice.group.service;

import jparest.practice.group.dto.*;
import jparest.practice.user.domain.User;

import java.util.List;

public interface GroupService {

    // 그룹 생성
    CreateGroupResponse createGroup(User user, CreateGroupRequest createGroupRequest);

    // 그룹 탈퇴
    Boolean withdrawGroup(User user, Long groupId);

    // 가입한 그룹 조회
    List<GetGroupUserResponse> getGroupUserList(User user);

    // 그룹 소유자 변경
    ChangeOwnerResponse changeOwner(User user, Long groupId, ChangeOwnerRequest changeOwnerRequest);

    // 그룹 검색
    List<SearchGroupListResponse> searchGroup(SearchGroupListRequest searchGroupListRequest);

}
