package jparest.practice.subscription.service;

import jparest.practice.subscription.dto.GetReceivedSubscriptionResponse;
import jparest.practice.subscription.dto.ProcessSubscriptionRequest;
import jparest.practice.subscription.dto.SubscribeForGroupRequest;
import jparest.practice.subscription.dto.SubscribeForGroupResponse;
import jparest.practice.user.domain.User;

import java.util.List;

public interface SubscriptionService {

    SubscribeForGroupResponse subscribeForGroup(User user, SubscribeForGroupRequest subscribeForGroupRequest);

    Boolean processSubscription(User user, Long subscriptionId, ProcessSubscriptionRequest processSubscriptionRequest);

    List<GetReceivedSubscriptionResponse> getReceivedSubscription(User user, Long groupId);
}
