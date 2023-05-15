package jparest.practice.user.dto;

import jparest.practice.user.domain.LoginType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SocialLoginResponse {
    private String socialUserId;
    private String email;
    private String nickname;
    private LoginType loginType;
}
