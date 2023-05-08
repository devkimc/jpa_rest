package jparest.practice.user.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jparest.practice.common.util.TokenDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserLoginResponse {
    private String email;
    private String name;
    private TokenDto tokenDto;

    public UserLoginResponse(String email, String name) {
        this.email = email;
        this.name = name;
    }

    public UserLoginResponse(String email, String name, TokenDto tokenDto) {
        this.email = email;
        this.name = name;
        this.tokenDto = tokenDto;
    }
}
