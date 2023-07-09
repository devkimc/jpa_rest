package jparest.practice.group.service;

import jparest.practice.group.domain.Group;
import jparest.practice.group.domain.GroupUser;
import jparest.practice.group.domain.GroupUserType;
import jparest.practice.group.dto.*;
import jparest.practice.group.exception.GroupAccessDeniedException;
import jparest.practice.group.exception.GroupUserNotFoundException;
import jparest.practice.group.repository.GroupQueryRepository;
import jparest.practice.group.repository.GroupRepository;
import jparest.practice.group.repository.GroupUserRepository;
import jparest.practice.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GroupServiceImpl implements GroupService {

    private final GroupRepository groupRepository;
    private final GroupUserRepository groupUserRepository;
    private final GroupQueryRepository groupQueryRepository;

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

        groupUserRepository.save(GroupUser.createGroupUser(saveGroup, user, GroupUserType.ROLE_OWNER));

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

        GroupUser findGroupUser = findGroupUser(user, groupId);

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

    /**
     * 그룹 소유권 양도
     */
    @Override
    @Transactional
    public ChangeOwnerResponse changeOwner(User user, Long groupId, ChangeOwnerRequest changeOwnerRequest) {

        List<GroupUser> groupUserList = findAllGroupUserByGroupId(groupId);

        // 1. 전임자의 역할 체크
        List<GroupUser> owners = groupUserList.stream()
                .filter(e -> e.getUser().getId().equals(user.getId()))
                .collect(Collectors.toList());

        if (owners.size() != 1) {
            throw new GroupAccessDeniedException("Not groupUserId = " + user.getId() + " , SameGroupUserCount = " + owners.size());
        }

        GroupUser findGroupOwner = owners.get(0);

        if (!findGroupOwner.getGroupUserType()
                .equals(GroupUserType.ROLE_OWNER)) {
            throw new GroupAccessDeniedException("Not ownerId = " + user.getId());
        }

        // 2. 후임자가 그룹원인지 체크
        List<GroupUser> successors = groupUserList.stream()
                .filter(e -> e.getUser().getId().equals(changeOwnerRequest.getSuccessorId()))
                .collect(Collectors.toList());

        if (successors.size() != 1) {
            throw new GroupUserNotFoundException("Not groupUserId = " + changeOwnerRequest.getSuccessorId() + " , SameGroupUserCount = " + owners.size());
        }

        // 3. 후임자 역할 변경
        GroupUser findGroupMember = successors.get(0);

        GroupUser groupSuccessor = GroupUser.builder()
                .id(findGroupMember.getId())
                .user(findGroupMember.getUser())
                .group(findGroupMember.getGroup())
                .groupUserType(GroupUserType.ROLE_OWNER)
                .build();

        groupUserRepository.save(groupSuccessor);

        // 4. 전임자 역할 변경
        GroupUser groupPredecessor = GroupUser.builder()
                .id(findGroupOwner.getId())
                .user(findGroupOwner.getUser())
                .group(findGroupOwner.getGroup())
                .groupUserType(GroupUserType.ROLE_MEMBER)
                .build();

        groupUserRepository.save(groupPredecessor);

        ChangeOwnerResponse changeOwnerResponse = ChangeOwnerResponse.builder()
                .ownerNickname(findGroupMember.getUser().getNickname())
                .updatedAt(LocalDateTime.now())
                .build();

        return changeOwnerResponse;
    }

    @Override
    public List<SearchGroupListResponse> searchGroup(SearchGroupListRequest searchGroupListRequest) {
        return groupQueryRepository.search(
                searchGroupListRequest.getGroupName(), searchGroupListRequest.getOwnerNickname()
        );
    }

    private GroupUser findGroupUser(User user, Long groupId) {
        return groupUserRepository.findByUserAndGroupId(user, groupId)
                .orElseThrow(() -> new GroupUserNotFoundException("userId = " + user.getId() + ", groupId = " + groupId));
    }

    private List<GroupUser> findAllGroupUserByGroupId(Long groupId) {
        return groupUserRepository.findAllByGroupId(groupId)
                .orElseThrow(() -> new GroupUserNotFoundException("groupId = " + groupId));
    }

}
