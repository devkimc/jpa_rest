package jparest.practice.common.utils;

import jparest.practice.user.domain.LoginType;
import jparest.practice.user.dto.SocialJoinRequest;

public class UserFixture {
    private static final String socialUserId1 = "123123";
    private static final String socialUserId2 = "234234";

    private static final String email1 = "eee@www.aaaa";
    private static final String email2 = "eee@www.bbb";

    private static final String nickname1 = "유저1";
    private static final String nickname2 = "유저2";

    private static final LoginType loginType1 = LoginType.KAKAO;
    private static final LoginType loginType2 = LoginType.KAKAO;

    public static SocialJoinRequest createFirstUser() {
        return new SocialJoinRequest(socialUserId1, email1, nickname1, loginType1);
    }

    public static SocialJoinRequest createSecondUser() {
        return new SocialJoinRequest(socialUserId2, email2, nickname2, loginType2);
    }
}
