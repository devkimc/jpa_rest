package jparest.practice.auth.security.dto;

import lombok.*;

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class LoginDTO {
    private String loginId;
    private String password;
}
