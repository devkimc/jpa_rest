package jparest.practice.group.service;

import jparest.practice.group.domain.Group;
import jparest.practice.group.domain.GroupUser;
import jparest.practice.group.domain.GroupUserType;
import jparest.practice.group.dto.CreateGroupRequest;
import jparest.practice.group.dto.CreateGroupResponse;
import jparest.practice.group.dto.GetGroupUserResponse;
import jparest.practice.group.exception.GroupUserNotFoundException;
import jparest.practice.group.repository.GroupRepository;
import jparest.practice.group.repository.GroupUserRepository;
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
    private final GroupUserRepository groupUserRepository;

    /**
     * 그룹 생성
     */
    @Override
    @Transactional
    public CreateGroupResponse createGroup(User user, CreateGroupRequest createGroupRequest) {
        Group newGroup = Group.builder()
                .groupName(createGroupRequest.getGroupName())
                .isPublic(createGroupRequest.getIsPublic())
                .build();

        Group saveGroup = groupRepository.save(newGroup);

        saveGroupUser(user, saveGroup);

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

        GroupUser findGroupUser = findGroupUser(user.getId(), groupId);

        Group group = findGroupUser.getGroup();

        int remainUserCount = group.getUserCount();

        // 탈퇴하는 유저가 그룹의 마지막 유저일 경우 그룹을 삭제한다.
        if (remainUserCount == 1) {
            groupRepository.delete(group);
            return true;
        }

        // 탈퇴하는 유저가 그룹의 마지막 유저가 아닐 경우 본인만 탈퇴한다.
        if (remainUserCount > 1) {
            groupUserRepository.delete(findGroupUser);
            return true;
        }

        return false;
    }

    /**
     * 그룹 리스트 조회
     */
    @Override
    @Transactional(readOnly = true)
    public List<GetGroupUserResponse> getGroupUserList(User user) {
        List<GroupUser> groupUsers = user.getGroupUsers();

        List<GetGroupUserResponse> getGroupUserResponse = new ArrayList<>(groupUsers.size());

        for (GroupUser groupUser : groupUsers) {
            Group group = groupUser.getGroup();
            GetGroupUserResponse res = new GetGroupUserResponse(group.getId(), group.getGroupName(), group.getUserCount());
            getGroupUserResponse.add(res);
        }

        return getGroupUserResponse;
    }

    private GroupUser findGroupUser(UUID userId, Long groupId) {
        return groupUserRepository.findByUserIdAndGroupId(userId, groupId)
                .orElseThrow(() -> new GroupUserNotFoundException("userId = " + userId + ", groupId = " + groupId));
    }

    private GroupUser saveGroupUser(User user, Group group) {
        GroupUser groupUser = GroupUser.builder()
                .user(user)
                .group(group)
                .groupUserType(GroupUserType.ROLE_OWNER)
                .build();

        GroupUser saveGroupUser = groupUserRepository.save(groupUser);
        saveGroupUser.addGroupUser();
        return saveGroupUser;
    }
}
