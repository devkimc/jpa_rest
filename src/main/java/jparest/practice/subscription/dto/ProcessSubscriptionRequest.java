package jparest.practice.subscription.dto;

import jparest.practice.subscription.domain.SubscriptionStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProcessSubscriptionRequest {
    private SubscriptionStatus status;
}
