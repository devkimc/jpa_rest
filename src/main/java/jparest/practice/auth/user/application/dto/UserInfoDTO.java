package jparest.practice.auth.user.application.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class UserInfoDTO {
    private long id;
    private String userId;
    private String email;
    private String name;
}
