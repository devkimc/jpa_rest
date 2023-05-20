package jparest.practice.group.service;

import jparest.practice.group.domain.Group;
import jparest.practice.group.domain.UserGroup;
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

    @Override
    public List<Group> getJoinGroupList(User user) {
        return null;
    }

    @Override
    public Boolean withdrawGroup(User user) {
        return null;
    }
}
