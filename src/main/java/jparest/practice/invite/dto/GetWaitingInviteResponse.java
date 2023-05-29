package jparest.practice.invite.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class GetWaitingInviteResponse {
    private Long inviteId;
    private String nickName;
    private String groupName;
}
