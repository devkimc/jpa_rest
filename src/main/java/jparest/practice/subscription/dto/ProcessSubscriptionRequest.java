package jparest.practice.subscription.dto;

import jparest.practice.subscription.domain.SubscriptionStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ProcessSubscriptionRequest {
    private SubscriptionStatus status;
}
