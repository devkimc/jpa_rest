package jparest.practice.subscription.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class SubscribeForGroupResponse {
    private Long subscriptionId;
}
