package jparest.practice.group.service;

import jparest.practice.group.domain.Group;
import jparest.practice.group.domain.UserGroup;
import jparest.practice.group.exception.GroupNotFoundException;
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

    private final UserRepository userRepository;
    private final GroupRepository groupRepository;
    private final UserGroupRepository userGroupRepository;


    /**
     * 그룹 생성
     */
    @Override
    @Transactional
    public Long addGroup(User user, String groupName) {
        Group newGroup = new Group(groupName);
        Group saveGroup = groupRepository.save(newGroup);

        UserGroup newUserGroup = new UserGroup(user, saveGroup);
        UserGroup saveUserGroup = userGroupRepository.save(newUserGroup);

        saveGroup.addUserGroup(saveUserGroup);

        return saveGroup.getId();
    }

    /**
     * 그룹 탈퇴
     */
    @Override
    @Transactional
    public Boolean withdrawGroup(User user, Long groupId) {
        Optional<UserGroup> findUserGroup = userGroupRepository.findAllByUserIdAndGroupId(user.getId(), groupId);

        userGroupRepository.delete(findUserGroup.get());

        Group group = findUserGroup.get().getGroup();
        int countUserOfGroup = group.getUserGroups().size();

        System.out.println("countUserOfGroup = " + countUserOfGroup);

        // 그룹의 마지막 멤버인지 확인
        if(countUserOfGroup == 1) {
            groupRepository.delete(group);
            return true;
        }

        if(countUserOfGroup > 2) return  true;

        return false;
    }
}
