package jparest.practice.group.service;

import jparest.practice.group.domain.Group;
import jparest.practice.group.domain.UserGroup;
import jparest.practice.group.dto.CreateGroupResponse;
import jparest.practice.group.dto.GetUserGroupResponse;
import jparest.practice.group.exception.UserGroupNotFoundException;
import jparest.practice.group.repository.GroupRepository;
import jparest.practice.group.repository.UserGroupRepository;
import jparest.practice.invite.repository.InviteRepository;
import jparest.practice.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GroupServiceImpl implements GroupService {

    private final GroupRepository groupRepository;
    private final UserGroupRepository userGroupRepository;
    private final InviteRepository inviteRepository;

    /**
     * 그룹 생성
     */
    @Override
    @Transactional
    public CreateGroupResponse createGroup(User user, String groupName) {
        Group newGroup = new Group(groupName);
        Group saveGroup = groupRepository.save(newGroup);

        saveUserGroup(user, saveGroup);

        return CreateGroupResponse.builder()
                .id(saveGroup.getId())
                .groupName(saveGroup.getGroupName())
                .build();
    }

    /**
     * 그룹 탈퇴
     */
    @Override
    @Transactional
    public Boolean withdrawGroup(User user, Long groupId) {

        UserGroup findUserGroup = findUserGroup(user.getId(), groupId);

        Group group = findUserGroup.getGroup();

        int remainUserCount = group.getUserCount();

        // 탈퇴하는 유저가 그룹의 마지막 유저일 경우 그룹을 삭제한다.
        if (remainUserCount == 1) {
            groupRepository.delete(group);
            return true;
        }

        // 탈퇴하는 유저가 그룹의 마지막 유저가 아닐 경우 본인만 탈퇴한다.
        if (remainUserCount > 1) {
            userGroupRepository.delete(findUserGroup);
            return true;
        }

        return false;
    }

    /**
     * 그룹 리스트 조회
     */
    @Override
    @Transactional(readOnly = true)
    public List<GetUserGroupResponse> getUserGroupList(User user) {
        List<UserGroup> userGroups = user.getUserGroups();

        List<GetUserGroupResponse> getUserGroupResponse = new ArrayList<>(userGroups.size());

        for (UserGroup userGroup: userGroups) {
            Group group = userGroup.getGroup();
            GetUserGroupResponse res = new GetUserGroupResponse(group.getId(), group.getGroupName(), group.getUserCount());
            getUserGroupResponse.add(res);
        }

        return getUserGroupResponse;
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
