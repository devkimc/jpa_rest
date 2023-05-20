package jparest.practice.user.dto;

import lombok.*;

@Getter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class UserLoginRequest {
    private String loginId;
    private String password;
}
