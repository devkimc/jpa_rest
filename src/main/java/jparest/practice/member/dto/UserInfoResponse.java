package jparest.practice.member.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class UserInfoResponse {
    private long id;
    private String userId;
    private String email;
    private String name;
}
