package jparest.practice.invite.service;

import jparest.practice.group.domain.Group;
import jparest.practice.group.domain.GroupUser;
import jparest.practice.group.domain.GroupUserType;
import jparest.practice.group.exception.ExistGroupUserException;
import jparest.practice.group.exception.GroupUserNotFoundException;
import jparest.practice.group.repository.GroupUserRepository;
import jparest.practice.invite.domain.Invite;
import jparest.practice.invite.domain.InviteStatus;
import jparest.practice.invite.dto.GetWaitingInviteResponse;
import jparest.practice.invite.dto.InviteUserRequest;
import jparest.practice.invite.dto.InviteUserResponse;
import jparest.practice.invite.dto.ProcessInvitationRequest;
import jparest.practice.invite.exception.ExistWaitingInviteException;
import jparest.practice.invite.exception.InviteNotFoundException;
import jparest.practice.invite.repository.InviteRepository;
import jparest.practice.user.domain.User;
import jparest.practice.user.exception.UserNotFoundException;
import jparest.practice.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static jparest.practice.invite.domain.InviteStatus.ACCEPT;
import static jparest.practice.invite.domain.InviteStatus.WAITING;

@Service
@RequiredArgsConstructor
public class InviteServiceImpl implements InviteService {

    private final UserRepository userRepository;
    private final GroupUserRepository groupUserRepository;
    private final InviteRepository inviteRepository;

    @Override
    @Transactional
    public InviteUserResponse inviteToGroup(User sendUser, InviteUserRequest inviteUserRequest) {

        Long groupId = inviteUserRequest.getGroupId();
        UUID recvUserId = inviteUserRequest.getRecvUserId();

        // 1. 초대한 사람이 그룹의 회원이 맞는지 확인
        GroupUser sendGroupUser = findGroupUser(sendUser.getId(), groupId);

        // 2. 그룹에 속한 유저를 초대했는지 확인
        if (sendGroupUser.getGroup().isJoinUser(recvUserId)) {
            throw new ExistGroupUserException("초대 불가능합니다. groupId = " + groupId + ", recvUserId = " + recvUserId);
        }

        // 3. 승인 대기중인 요청이 존재하는지 확인
        Optional<Invite> waitingInvite = inviteRepository.
                findBySendGroupUserIdAndRecvUserIdAndStatus(sendGroupUser.getId(), recvUserId, WAITING);

        if(waitingInvite.isPresent()) {
            throw new ExistWaitingInviteException("대기중인 inviteId = " + waitingInvite.get().getId());
        }

        User recvUser = findUser(recvUserId);
        Invite invite = inviteRepository.save(Invite.createInvite(sendGroupUser, recvUser));

        return InviteUserResponse.builder().inviteId(invite.getId()).build();
    }

    @Override
    @Transactional
    public boolean processInvitation(Long inviteId, User user, ProcessInvitationRequest processInvitationRequest) {
        Invite invite = findInvite(inviteId);

        InviteStatus requestStatus = processInvitationRequest.getStatus();

        invite.chkAuthorizationOfInvitation(user, requestStatus);

        if (requestStatus == ACCEPT) {
            Group group = invite.getSendGroupUser().getGroup();
            saveGroupUser(user, group);
        }

        updateStatus(invite, requestStatus);
        return true;
    }

    @Override
    @Transactional(readOnly = true)
    public List<GetWaitingInviteResponse> getWaitingInviteList(User user) {
        List<Invite> invites = findInviteAllByUserIdAndStatus(user.getId(), WAITING);

        if(invites == null) {
            return new ArrayList<>(0);
        }

        List<GetWaitingInviteResponse> result = new ArrayList<GetWaitingInviteResponse>(invites.size());

        for (Invite invite : invites
        ) {
            result.add(GetWaitingInviteResponse.builder()
                    .inviteId(invite.getId())
                    .nickName(invite.getSendGroupUser().getUser().getNickname())
                    .groupName(invite.getSendGroupUser().getGroup().getGroupName())
                    .build());
        }
        return result;
    }

    private List<Invite> findInviteAllByUserIdAndStatus(UUID userId, InviteStatus status) {
        return inviteRepository.findAllByRecvUserIdAndStatus(userId, status).get();
    }

    private Invite findInvite(Long inviteId) {
        return inviteRepository.findById(inviteId).orElseThrow(() -> new InviteNotFoundException("inviteId = " + inviteId));
    }

    private User findUser(UUID userId) {
        return userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("userId = " + userId));
    }

    private GroupUser findGroupUser(UUID userId, Long groupId) {
        return groupUserRepository.findByUserIdAndGroupId(userId, groupId)
                .orElseThrow(() -> new GroupUserNotFoundException("userId = " + userId + ", groupId = " + groupId));
    }
    
    private void updateStatus(Invite invite, InviteStatus status) {
        invite.updateStatus(status);
        inviteRepository.save(invite);
    }

    private GroupUser saveGroupUser(User user, Group group) {
        GroupUser groupUser = GroupUser.builder()
                .user(user)
                .group(group)
                .groupUserType(GroupUserType.ROLE_MEMBER)
                .build();

        GroupUser saveGroupUser = groupUserRepository.save(groupUser);
        saveGroupUser.addGroupUser();
        return saveGroupUser;
    }
}
