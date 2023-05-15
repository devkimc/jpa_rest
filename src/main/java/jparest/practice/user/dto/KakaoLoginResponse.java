package jparest.practice.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class KakaoLoginResponse {
    private Long socialUserId;
    private String email;
    private String nickname;
}
