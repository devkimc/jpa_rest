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
import jparest.practice.invite.dto.ProcessInviteRequest;
import jparest.practice.invite.exception.AlreadyProcessedInviteException;
import jparest.practice.invite.exception.ExistWaitingInviteException;
import jparest.practice.invite.exception.InviteNotFoundException;
import jparest.practice.invite.exception.NotValidUpdateInviteStatusException;
import jparest.practice.invite.repository.InviteRepository;
import jparest.practice.user.domain.User;
import jparest.practice.user.exception.UserNotFoundException;
import jparest.practice.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        GroupUser sendGroupUser = findGroupUser(sendUser, groupId);

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
    public boolean processInvite(User user, Long inviteId, ProcessInviteRequest processInviteRequest) {

        Invite invite = findInvite(inviteId);
        InviteStatus requestStatus = processInviteRequest.getStatus();

        // 1. 현재 초대 상태에 대한 유효성 검사
        if (invite.getStatus() != WAITING) {
            throw new AlreadyProcessedInviteException(
                    "inviteId = " + inviteId + ", userId = " + user.getId() + ", status = " + invite.getStatus());
        }

        // 2. 변경 상태에 대한 유효성 검사
        if (requestStatus == WAITING) {
            throw new NotValidUpdateInviteStatusException(
                    "inviteId = " + inviteId + ", userId = " + user.getId() + ", status = WAITING");
        }

        // 3. 초대를 처리하는 유저가 권한이 있는지 확인
        invite.chkAuthorizationOfInviteProcess(user, requestStatus);

        // 4. 초대를 받은 유저가 수락할 경우 그룹원으로 추가
        if (requestStatus == ACCEPT) {
            Group group = invite.getSendGroupUser().getGroup();
            GroupUser groupUser = GroupUser.createGroupUser(group, user, GroupUserType.ROLE_MEMBER);

            groupUserRepository.save(groupUser);
        }

        // 5. 초대 상태 변경
        invite.updateStatus(requestStatus);
        inviteRepository.save(invite);

        return true;
    }

    @Override
    @Transactional(readOnly = true)
    public List<GetWaitingInviteResponse> getWaitingInviteList(User user) {
        return inviteRepository.findAllByRecvUserIdAndStatus(user.getId(), WAITING);
    }

    private Invite findInvite(Long inviteId) {
        return inviteRepository.findById(inviteId).orElseThrow(() -> new InviteNotFoundException("inviteId = " + inviteId));
    }

    private User findUser(UUID userId) {
        return userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("userId = " + userId));
    }

    private GroupUser findGroupUser(User user, Long groupId) {
        return groupUserRepository.findByUserAndGroupId(user, groupId)
                .orElseThrow(() -> new GroupUserNotFoundException("userId = " + user.getId() + ", groupId = " + groupId));
    }
}
