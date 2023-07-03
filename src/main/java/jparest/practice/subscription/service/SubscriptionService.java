package jparest.practice.subscription.service;

import jparest.practice.subscription.dto.CreateSubscriptionRequest;
import jparest.practice.user.domain.User;

public interface SubscriptionService {

    Boolean createSubscription(User user, CreateSubscriptionRequest createSubscriptionRequest);
}
