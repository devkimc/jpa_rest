package jparest.practice.user.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserJoinRequest {
    private String loginId;
    private String password;
    private String email;
    private String name;
}
