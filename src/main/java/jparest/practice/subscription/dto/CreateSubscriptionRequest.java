package jparest.practice.subscription.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateSubscriptionRequest {
    private Long groupId;

    @Size(max = 40, message = "가입 신청 메시지는 40 글자 이하여야 합니다.")
    private String message;
}
