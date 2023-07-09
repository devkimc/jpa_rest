package jparest.practice.subscription.service;

import jparest.practice.group.domain.Group;
import jparest.practice.group.domain.GroupUser;
import jparest.practice.group.domain.GroupUserType;
import jparest.practice.group.exception.ExistGroupUserException;
import jparest.practice.group.exception.GroupNotFoundException;
import jparest.practice.group.repository.GroupRepository;
import jparest.practice.group.repository.GroupUserRepository;
import jparest.practice.invite.exception.NotValidUpdateInviteStatusException;
import jparest.practice.subscription.domain.Subscription;
import jparest.practice.subscription.domain.SubscriptionStatus;
import jparest.practice.subscription.dto.GetReceivedSubscriptionResponse;
import jparest.practice.subscription.dto.ProcessSubscriptionRequest;
import jparest.practice.subscription.dto.SubscribeForGroupRequest;
import jparest.practice.subscription.dto.SubscribeForGroupResponse;
import jparest.practice.subscription.exception.AlreadyProcessedSubscriptionException;
import jparest.practice.subscription.exception.ExistWaitingSubscriptionException;
import jparest.practice.subscription.exception.SubscriptionNotFoundException;
import jparest.practice.subscription.repository.SubscriptionRepository;
import jparest.practice.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static jparest.practice.subscription.domain.SubscriptionStatus.*;

@Service
@RequiredArgsConstructor
public class SubscriptionServiceImpl implements SubscriptionService {

    private final GroupRepository groupRepository;
    private final GroupUserRepository groupUserRepository;
    private final SubscriptionRepository subscriptionRepository;

    @Override
    @Transactional
    public SubscribeForGroupResponse subscribeForGroup(User user, SubscribeForGroupRequest subscribeForGroupRequest) {

        // 1. 가입신청을 한 유저가 이미 그룹에 속한 유저인지 확인
        Long groupId = subscribeForGroupRequest.getGroupId();

        if (user.isJoinGroup(groupId)) {
            throw new ExistGroupUserException("가입신청 불가능합니다. groupId = " + groupId + ", userId = " + user.getId());
        }

        // 2. 승인 대기중인 요청이 존재하는지 확인
        Optional<Subscription> waitingSubscription = subscriptionRepository.
                findByApplicantIdAndGroupIdAndStatus(user.getId(), groupId, WAITING);

        if (waitingSubscription.isPresent()) {
            throw new ExistWaitingSubscriptionException("대기중인 subscriptionId = " + waitingSubscription.get().getId());
        }

        // 3. 가입 승인 요청
        Group findGroup = findGroupById(groupId);
        Subscription subscription = Subscription.createSubscription(user, findGroup, subscribeForGroupRequest.getMessage());

        Subscription saveSubscription = subscriptionRepository.save(subscription);

        return SubscribeForGroupResponse.builder()
                .subscriptionId(saveSubscription.getId())
                .build();
    }

    @Override
    public Boolean processSubscription(User user, Long subscriptionId, ProcessSubscriptionRequest processSubscriptionRequest) {

        Subscription subscription = findSubscriptionById(subscriptionId);
        SubscriptionStatus requestStatus = processSubscriptionRequest.getStatus();

        // 1. 현재 가입신청 상태에 대한 유효성 검사
        if (subscription.getStatus() != WAITING) {
            throw new AlreadyProcessedSubscriptionException(
                    "subscriptionId = " + subscriptionId + ", userId = " + user.getId() + ", status = " + subscription.getStatus());
        }

        // 2. 변경 상태에 대한 유효성 검사
        if (requestStatus == WAITING) {
            throw new NotValidUpdateInviteStatusException(
                    "subscriptionId = " + subscriptionId + ", userId = " + user.getId() + ", status = WAITING");
        }

        // 3. 가입신청 처리에 대한 권한 검사
        subscription.chkAuthorizationOfSubscriptionProcess(user, requestStatus);

        // 4. 수락이라면 그룹원으로 추가
        if (requestStatus == ACCEPT) {
            GroupUser groupApplicant = GroupUser.createGroupUser(
                    subscription.getGroup(), subscription.getApplicant(), GroupUserType.ROLE_MEMBER);

            groupUserRepository.save(groupApplicant);
        }

        // 5. 가입신청 상태 업데이트
        subscription.updateStatus(requestStatus);
        subscriptionRepository.save(subscription);

        return true;
    }

    @Override
    @Transactional(readOnly = true)
    public List<GetReceivedSubscriptionResponse> getReceivedSubscription(User user, Long groupId) {
        return subscriptionRepository.findAllByGroupIdAndStatus(groupId, WAITING);
    }

    private Group findGroupById(Long groupId) {
        return groupRepository.findById(groupId)
                .orElseThrow(() -> new GroupNotFoundException("groupId = " + groupId));
    }

    private Subscription findSubscriptionById(Long subscriptionId) {
        return subscriptionRepository.findById(subscriptionId)
                .orElseThrow(() -> new SubscriptionNotFoundException("subscriptionId = " + subscriptionId));
    }
}
