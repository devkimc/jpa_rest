package jparest.practice.subscription.service;

import jparest.practice.group.domain.Group;
import jparest.practice.group.exception.ExistGroupUserException;
import jparest.practice.group.exception.GroupNotFoundException;
import jparest.practice.group.repository.GroupRepository;
import jparest.practice.subscription.domain.Subscription;
import jparest.practice.subscription.domain.SubscriptionStatus;
import jparest.practice.subscription.dto.SubscribeForGroupRequest;
import jparest.practice.subscription.dto.SubscribeForGroupResponse;
import jparest.practice.subscription.exception.ExistWaitingSubscriptionException;
import jparest.practice.subscription.repository.SubscriptionRepository;
import jparest.practice.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SubscriptionServiceImpl implements SubscriptionService {

    private final GroupRepository groupRepository;
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
                findByApplicantIdAndGroupIdAndStatus(user.getId(), groupId, SubscriptionStatus.WAITING);

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

    private Group findGroupById(Long groupId) {
        return groupRepository.findById(groupId)
                .orElseThrow(() -> new GroupNotFoundException("groupId = " + groupId));
    }
}
