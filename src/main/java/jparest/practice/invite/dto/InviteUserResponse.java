package jparest.practice.invite.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class InviteUserResponse {
    private Long inviteId;
}
