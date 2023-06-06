package jparest.practice.invite.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class InviteUserRequest {
    private UUID recvUserId;
    private Long groupId;
}
