package jparest.practice.auth.user.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserJoinDTO {
    private String loginId;
    private String password;
    private String email;
    private String name;
}
