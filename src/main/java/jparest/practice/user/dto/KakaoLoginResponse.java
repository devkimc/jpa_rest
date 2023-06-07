package jparest.practice.user.dto;

import jparest.practice.common.util.TokenDto;
import jparest.practice.user.domain.LoginType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KakaoLoginResponse {
    private String socialUserId;
    private String email;
    private String nickname;
    private LoginType loginType;
    private TokenDto tokenDto;
}
