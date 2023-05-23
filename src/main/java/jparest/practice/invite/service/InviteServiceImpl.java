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
import jparest.practice.invite.exception.InviteNotFoundException;
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

    @Override
    @Transactional
    public Invite inviteToGroup(Long groupId, User sendUser, UUID recvUserId) {

        // 1. 초대한 사람이 그룹의 회원이 맞는지 확인
        UserGroup sendUserGroup = findUserGroup(sendUser.getId(), groupId);
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

        User recvUser = findUser(recvUserId);
        Invite invite = inviteRepository.save(new Invite(sendUserGroup, recvUser, InviteStatus.WAITING));

        return invite;
    }

    @Override
    @Transactional
    public Group agreeInvitation(Long inviteId, User recvUser) {
        inviteRepository.updateWaitingInviteStatus(inviteId, recvUser.getId(), InviteStatus.ACCEPT)
                .orElseThrow(() -> new InviteNotFoundException("inviteId = " + inviteId));

        Invite invite = findInvite(inviteId);

        return invite.getSendUserGroup().getGroup();
    }

    @Override
    public boolean rejectInvitation(Long inviteId, User recvUser) {
        return false;
    }

    @Override
    public boolean cancelInvitation(Long inviteId, User recvUser) {
        return false;
    }


    private Invite findInvite(Long inviteId) {
        return inviteRepository.findById(inviteId).orElseThrow(() -> new InviteNotFoundException("inviteId = " + inviteId));
    }

    private User findUser(UUID userId) {
        return userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("userId = " + userId));
    }

    private Group findGroup(Long groupId) {
        return groupRepository.findById(groupId).orElseThrow(() -> new GroupNotFoundException("groupId = " + groupId));
    }

    private UserGroup findUserGroup(UUID userId, Long groupId) {
        return userGroupRepository.findByUserIdAndGroupId(userId, groupId)
                .orElseThrow(() -> new UserGroupNotFoundException("userId = " + userId + ", groupId = " + groupId));
    }
}
