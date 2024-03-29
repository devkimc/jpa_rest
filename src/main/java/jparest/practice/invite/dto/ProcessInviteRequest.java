package jparest.practice.invite.dto;

import jparest.practice.invite.domain.InviteStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProcessInviteRequest {

    private InviteStatus status;
}
