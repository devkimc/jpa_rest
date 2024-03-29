package jparest.practice.user.dto;

import jparest.practice.user.domain.LoginType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class SocialJoinRequest {
    private String socialUserId;
    private String email;
    private String nickname;
    private LoginType loginType;
}
