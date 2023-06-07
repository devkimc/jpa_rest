package jparest.practice.common.utils.fixture;

import jparest.practice.user.domain.LoginType;
import jparest.practice.user.dto.SocialJoinRequest;

import java.util.UUID;

public class UserFixture {
    public static final String token = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJBQ0NFU1NfVE9LRU4iLCJhdXRoIjoiUk9MRV9HRU5FUkFMIiwidXNlcklkIjoiMTIzMTIzMTMxMjMxMjMiLCJpc3MiOiJqcGEucmVzdC53c2tpbSIsImlhdCI6MTY4NjEwMzA4NiwiZXhwIjoxNjg2MTAzMDg5fQ.JY0kvn7RBCdDGi8Pakk71EKgy0LF9aHw9LtFqugUeBQ";
    public static final UUID userId = UUID.fromString("7f616b44-da03-43f7-8399-8242127720ee");
    public static final String socialUserId1 = "123123";
    public static final String socialUserId2 = "234234";

    public static final String email1 = "eee@www.aaaa";
    public static final String email2 = "eee@www.bbb";

    public static final String nickname1 = "유저1";
    public static final String nickname2 = "유저2";

    public static final LoginType loginType1 = LoginType.KAKAO;
    public static final LoginType loginType2 = LoginType.KAKAO;

    public static SocialJoinRequest createFirstUser() {
        return new SocialJoinRequest(socialUserId1, email1, nickname1, loginType1);
    }

    public static SocialJoinRequest createSecondUser() {
        return new SocialJoinRequest(socialUserId2, email2, nickname2, loginType2);
    }
}
