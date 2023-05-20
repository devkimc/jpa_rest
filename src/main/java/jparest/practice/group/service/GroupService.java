package jparest.practice.group.service;

import jparest.practice.group.domain.Group;
import jparest.practice.user.domain.User;

import java.util.List;
import java.util.UUID;

public interface GroupService {

    // 그룹 생성
    Long addGroup(User user, String groupName);

    // 가입된 그룹 조회
    List<Group> getJoinGroupList(User user);

    // 그룹 탈퇴
    Boolean withdrawGroup(User user);
}
