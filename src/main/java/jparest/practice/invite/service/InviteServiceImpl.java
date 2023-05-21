package jparest.practice.invite.service;

import jparest.practice.group.domain.Group;
import jparest.practice.group.domain.UserGroup;
import jparest.practice.group.exception.ExistUserGroupException;
import jparest.practice.group.exception.GroupNotFoundException;
import jparest.practice.group.exception.UserGroupNotFoundException;
import jparest.practice.group.repository.GroupRepository;
import jparest.practice.group.repository.UserGroupRepository;
import jparest.practice.invite.domain.Invite;
import jparest.practice.invite.domain.InviteStatus;
import jparest.practice.invite.exception.ExistInviteForUserException;
import jparest.practice.invite.repository.InviteRepository;
import jparest.practice.user.domain.User;
import jparest.practice.user.exception.UserNotFoundException;
import jparest.practice.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class InviteServiceImpl implements InviteService {

    private final UserRepository userRepository;
    private final GroupRepository groupRepository;
    private final UserGroupRepository userGroupRepository;
    private final InviteRepository inviteRepository;


    /**
     * 그룹 초대
     */
    @Transactional
    public Long invite(Long memberId, Long groupId, Long recvUserId) {

        // 엔티티 조회
//        GroupMember groupMember = groupRepository.findByGroupIdAndMemberId(groupId, memberId);

        // 그룹 초대 생성
//        Invite invite = Invite.createInvite(recvUserId, groupMember);

        // 초대 저장
//        inviteRepository.save(invite);
//        return invite.getId();

        // TODO: 로직 수정후 변경하기
        return 1L;
    }

    @Override
    @Transactional
    public boolean inviteToGroup(Long groupId, UUID sendUserId, UUID recvUserId) {

        // 1. 초대한 사람이 그룹의 회원이 맞는지 확인
        UserGroup sendUserGroup = getFindUserGroup(sendUserId, groupId);
        Long findUserGroupId = sendUserGroup.getId();

        // 2. 그룹에 속한 유저를 초대했는지 확인
        Optional<UserGroup> existUserGroup = userGroupRepository.findByUserIdAndGroupId(recvUserId, groupId);

        if (existUserGroup.isPresent()) {
            throw new ExistUserGroupException("groupId = " + groupId);
        }

        // 3. 대기중인 요청이 존재하는지 확인
        Optional<Invite> waitingInvite = inviteRepository.
                findBySendUserGroupIdAndRecvUserIdAndInviteStatus(findUserGroupId, recvUserId, InviteStatus.WAITING);

        if(waitingInvite.isPresent()) {
            throw new ExistInviteForUserException("대기중인 inviteId = " + waitingInvite.get().getId());
        }

        User recvUser = getFindUser(recvUserId);
        inviteRepository.save(new Invite(sendUserGroup, recvUser, InviteStatus.WAITING));

        return true;
    }

    @Override
    @Transactional
    public boolean agreeInvitation(Long inviteId, UUID recvUserId) {
        return false;
    }

    @Override
    public boolean rejectInvitation(Long inviteId, UUID recvUserId) {
        return false;
    }

    @Override
    public boolean cancelInvitation(Long inviteId, UUID recvUserId) {
        return false;
    }

    private User getFindUser(UUID userId) {
        return userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("userId = " + userId));
    }

    private Group getFindGroup(Long groupId) {
        return groupRepository.findById(groupId).orElseThrow(() -> new GroupNotFoundException("groupId = " + groupId));
    }

    private UserGroup getFindUserGroup(UUID userId, Long groupId) {
        return userGroupRepository.findByUserIdAndGroupId(userId, groupId)
                .orElseThrow(() -> new UserGroupNotFoundException("userId = " + userId + ", groupId = " + groupId));
    }
}
