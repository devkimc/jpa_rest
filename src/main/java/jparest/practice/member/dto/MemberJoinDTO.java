package jparest.practice.member.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MemberJoinDTO {
    private String loginId;
    private String password;
    private String email;
    private String name;
}
