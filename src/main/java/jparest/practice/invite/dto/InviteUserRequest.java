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

    @Min(1)
    private Long groupId;
}
