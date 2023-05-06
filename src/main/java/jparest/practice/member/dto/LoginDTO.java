package jparest.practice.member.dto;

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
