package jparest.practice.common.fixture;

import jparest.practice.user.domain.LoginType;
import jparest.practice.user.dto.SocialJoinRequest;

import java.util.UUID;

public class UserFixture {
    public static final String token = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJBQ0NFU1NfVE9LRU4iLCJhdXRoIjoiUk9MRV9HRU5FUkFMIiwidXNlcklkIjoiMTIzMTIzMTMxMjMxMjMiLCJpc3MiOiJqcGEucmVzdC53c2tpbSIsImlhdCI6MTY4NjEwMzA4NiwiZXhwIjoxNjg2MTAzMDg5fQ.JY0kvn7RBCdDGi8Pakk71EKgy0LF9aHw9LtFqugUeBQ";
    public static final UUID userId1 = UUID.fromString("43f28ebf-8921-477c-8089-dfa50d587697");
    public static final UUID userId2 = UUID.fromString("1ee2aee3-1410-4f00-8a4e-447459bf7c90");
    public static final String socialUserId1 = "123123";
    public static final String socialUserId2 = "234234";
    public static final String socialUserId3 = "356789";

    public static final String email1 = "eee@www.aaaa";
    public static final String email2 = "eee@www.bbb";
    public static final String email3 = "eee@www.ccc";

    public static final String nickname1 = "유저1";
    public static final String nickname2 = "유저2";
    public static final String nickname3 = "유저3";

    public static final LoginType loginType1 = LoginType.KAKAO;
    public static final LoginType loginType2 = LoginType.KAKAO;
    public static final LoginType loginType3 = LoginType.KAKAO;

    public static SocialJoinRequest createFirstUser() {
        return new SocialJoinRequest(socialUserId1, email1, nickname1, loginType1);
    }

    public static SocialJoinRequest createSecondUser() {
        return new SocialJoinRequest(socialUserId2, email2, nickname2, loginType2);
    }

    public static SocialJoinRequest createThirdUser() {
        return new SocialJoinRequest(socialUserId3, email3, nickname3, loginType3);
    }
}
