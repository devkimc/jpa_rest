package jparest.practice.invite.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class InviteUserRequest {

    private UUID recvUserId;

    @Min(value = 1, message = "유효하지 않은 그룹 아이디입니다.")
    private Long groupId;
}
