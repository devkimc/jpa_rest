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
import jparest.practice.invite.dto.GetWaitingInviteResponse;
import jparest.practice.invite.dto.InviteStatusPatchRequest;
import jparest.practice.invite.dto.InviteUserRequest;
import jparest.practice.invite.dto.InviteUserResponse;
import jparest.practice.invite.exception.AlreadyProcessedInviteException;
import jparest.practice.invite.exception.ExistInviteForUserException;
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

import static jparest.practice.invite.domain.InviteStatus.*;

@Service
@RequiredArgsConstructor
public class InviteServiceImpl implements InviteService {

    private final UserRepository userRepository;
    private final UserGroupRepository userGroupRepository;
    private final InviteRepository inviteRepository;

    @Override
    @Transactional
    public InviteUserResponse inviteToGroup(User sendUser, InviteUserRequest inviteUserRequest) {

        Long groupId = inviteUserRequest.getGroupId();
        UUID recvUserId = inviteUserRequest.getRecvUserId();

        // 1. 초대한 사람이 그룹의 회원이 맞는지 확인
        UserGroup sendUserGroup = findUserGroup(sendUser.getId(), groupId);

        // 2. 그룹에 속한 유저를 초대했는지 확인
        if (sendUserGroup.getGroup().isJoinUser(recvUserId)) {
            throw new ExistUserGroupException("groupId = " + groupId);
        }

        // 3. 대기중인 요청이 존재하는지 확인
        Optional<Invite> waitingInvite = inviteRepository.
                findBySendUserGroupIdAndRecvUserIdAndInviteStatus(sendUserGroup.getId(), recvUserId, WAITING);

        if(waitingInvite.isPresent()) {
            throw new ExistInviteForUserException("대기중인 inviteId = " + waitingInvite.get().getId());
        }

        User recvUser = findUser(recvUserId);
        Invite invite = inviteRepository.save(new Invite(sendUserGroup, recvUser, WAITING));

        return InviteUserResponse.builder().inviteId(invite.getId()).build();
    }

    @Override
    @Transactional
    public boolean procInvitation(Long inviteId, User user, InviteStatus requestStatus) {
        Invite invite = findInvite(inviteId);

        invite.chkAuthorizationOfInvitation(user, requestStatus);

        if (requestStatus == ACCEPT) {
            Group group = invite.getSendUserGroup().getGroup();
            saveUserGroup(user, group);
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
                    .nickName(invite.getSendUserGroup().getUser().getNickname())
                    .groupName(invite.getSendUserGroup().getGroup().getGroupName())
                    .build());
        }
        return result;
    }

    private List<Invite> findInviteAllByUserIdAndStatus(UUID userId, InviteStatus status) {
        return inviteRepository.findAllByRecvUserIdAndInviteStatus(userId, status).get();
    }

    private Invite findInvite(Long inviteId) {
        return inviteRepository.findById(inviteId).orElseThrow(() -> new InviteNotFoundException("inviteId = " + inviteId));
    }

    private User findUser(UUID userId) {
        return userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("userId = " + userId));
    }

    private UserGroup findUserGroup(UUID userId, Long groupId) {
        return userGroupRepository.findByUserIdAndGroupId(userId, groupId)
                .orElseThrow(() -> new UserGroupNotFoundException("userId = " + userId + ", groupId = " + groupId));
    }
    
    private void updateStatus(Invite invite, InviteStatus inviteStatus) {
        invite.updateStatus(inviteStatus);
        inviteRepository.save(invite);
    }

    private UserGroup saveUserGroup(User user, Group group) {
        UserGroup userGroup = userGroupRepository.save(new UserGroup(user, group));
        userGroup.addUserGroup();
        return userGroup;
    }
}
