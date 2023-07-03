package jparest.practice.subscription.service;

import jparest.practice.group.exception.ExistGroupUserException;
import jparest.practice.subscription.domain.Subscription;
import jparest.practice.subscription.domain.SubscriptionStatus;
import jparest.practice.subscription.dto.CreateSubscriptionRequest;
import jparest.practice.subscription.repository.SubscriptionRepository;
import jparest.practice.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SubscriptionServiceImpl implements SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;

    @Override
    @Transactional
    public Boolean createSubscription(User user, CreateSubscriptionRequest createSubscriptionRequest) {

        // 1. 가입신청을 한 유저가 이미 그룹에 속한 유저인지 확인
        Long groupId = createSubscriptionRequest.getGroupId();

        if (user.isJoinGroup(groupId)) {
            throw new ExistGroupUserException("가입신청 불가능합니다. groupId = " + groupId + ", userId = " + user.getId());
        }

        // 2. 승인 대기중인 요청이 존재하는지 확인
        Optional<Subscription> waitingSubscription = subscriptionRepository.
                findByApplicantIdAndGroupIdAndStatus(user.getId(), groupId, SubscriptionStatus.WAITING);

        if (waitingSubscription.isPresent()) {
//            throw
        }

        // 3. 가입 승인 요청


        return null;
    }
}
