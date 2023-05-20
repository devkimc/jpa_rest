package jparest.practice.group.service;

import jparest.practice.group.domain.Group;

import java.util.List;
import java.util.UUID;

public interface GroupService {

    // 그룹 생성
    Long addGroup(UUID userId, String groupName);

    // 그룹 조회
    List<Group> getGroupList(UUID userId);

    // 그룹 탈퇴
    Boolean withdrawGroup(UUID userId);
}
