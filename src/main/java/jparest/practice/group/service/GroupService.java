package jparest.practice.group.service;

import jparest.practice.group.domain.Group;
import jparest.practice.group.domain.UserGroup;
import jparest.practice.user.domain.User;

import java.util.List;
import java.util.UUID;

public interface GroupService {

    // 그룹 생성
    Group createGroup(User user, String groupName);

    UserGroup addUserGroup(User user, Long groupId);

    // 그룹 탈퇴
    Boolean withdrawGroup(User user, Long groupId);
}
