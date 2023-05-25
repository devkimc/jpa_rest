package jparest.practice.invite.dto;

import jparest.practice.invite.domain.InviteStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class InviteStatusPatchRequest {
    private InviteStatus inviteStatus;
}
