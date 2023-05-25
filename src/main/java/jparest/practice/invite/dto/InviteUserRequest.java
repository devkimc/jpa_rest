package jparest.practice.invite.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor
public class InviteUserRequest {
    private UUID recvUserId;
    private Long groupId;
}
