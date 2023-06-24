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

    // 그룹 소유권 양도
    TransferOwnershipOfGroupResponse transferOwnershipOfGroup(User user,
                                                              TransferOwnershipOfGroupRequest transferOwnershipOfGroupRequest);
}
