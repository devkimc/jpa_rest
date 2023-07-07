package jparest.practice.subscription.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetReceivedSubscriptionResponse {
    private Long subscriptionId;
    private UUID applicantId;
    private String applicantNickname;
    private String message;
    private LocalDateTime createdAt;
}
