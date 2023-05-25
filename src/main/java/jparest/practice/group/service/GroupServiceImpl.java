package jparest.practice.group.service;

import jparest.practice.group.domain.Group;
import jparest.practice.group.domain.UserGroup;
import jparest.practice.group.dto.CreateGroupResponse;
import jparest.practice.group.exception.GroupNotFoundException;
import jparest.practice.group.exception.UserGroupNotFoundException;
import jparest.practice.group.repository.GroupRepository;
import jparest.practice.group.repository.UserGroupRepository;
import jparest.practice.user.domain.User;
import jparest.practice.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GroupServiceImpl implements GroupService {

    private final GroupRepository groupRepository;
    private final UserGroupRepository userGroupRepository;

    /**
     * 그룹 생성
     */
    @Override
    @Transactional
    public Group createGroup(User user, String groupName) {
        Group newGroup = new Group(groupName);
        Group saveGroup = groupRepository.save(newGroup);

        saveUserGroup(user, saveGroup);

        return saveGroup;
    }

    /**
     * 그룹 탈퇴
     */
    @Override
    @Transactional
    public Boolean withdrawGroup(User user, Long groupId) {

        UserGroup findUserGroup = findUserGroup(user.getId(), groupId);

        userGroupRepository.delete(findUserGroup);

        Group group = findUserGroup.getGroup();
        int countUserOfGroup = group.getUserGroups().size();

        // 그룹의 마지막 멤버인지 확인
        if(countUserOfGroup == 1) {
            groupRepository.delete(group);
            return true;
        }

        if(countUserOfGroup > 2) return  true;

        return false;
    }

    private Group findGroup(Long groupId) {
        return groupRepository.findById(groupId)
                .orElseThrow(() -> new GroupNotFoundException("groupId = " + groupId));
    }

    private UserGroup findUserGroup(UUID userId, Long groupId) {
        return userGroupRepository.findByUserIdAndGroupId(userId, groupId)
                .orElseThrow(() -> new UserGroupNotFoundException("userId = " + userId + ", groupId = " + groupId));
    }

    private UserGroup saveUserGroup(User user, Group group) {
        UserGroup userGroup = userGroupRepository.save(new UserGroup(user, group));
        userGroup.addUserGroup();
        return userGroup;
    }
}
