package jparest.practice.subscription.controller;

import jparest.practice.auth.security.CurrentUser;
import jparest.practice.common.util.ApiResult;
import jparest.practice.common.util.ApiUtils;
import jparest.practice.subscription.dto.ProcessSubscriptionRequest;
import jparest.practice.subscription.dto.SubscribeForGroupRequest;
import jparest.practice.subscription.dto.SubscribeForGroupResponse;
import jparest.practice.subscription.service.SubscriptionService;
import jparest.practice.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/subscription")
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    @PostMapping
    ApiResult<SubscribeForGroupResponse> subscribeForGroup(@CurrentUser User user,
                                                           @Valid @RequestBody SubscribeForGroupRequest subscribeForGroupRequest) {
        return ApiUtils.success(subscriptionService.subscribeForGroup(user, subscribeForGroupRequest));
    }

    @PatchMapping(value = "/{subscriptionId}/status")
    ApiResult<Boolean> processSubscription(@CurrentUser User user,
                                           @PathVariable Long subscriptionId,
                                           @RequestBody ProcessSubscriptionRequest processSubscriptionRequest) {
        return ApiUtils.success(subscriptionService.processSubscription(user, subscriptionId, processSubscriptionRequest));
    }
}
