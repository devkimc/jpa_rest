package jparest.practice.group.service;

import jparest.practice.group.domain.Group;
import jparest.practice.group.dto.CreateGroupResponse;
import jparest.practice.user.domain.User;

public interface GroupService {

    // 그룹 생성

    Group createGroup(User user, String groupName);

    // 그룹 탈퇴
    Boolean withdrawGroup(User user, Long groupId);
}
