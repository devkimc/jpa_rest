package jparest.practice.common;

import jparest.practice.user.domain.LoginType;
import jparest.practice.user.domain.User;
import jparest.practice.user.dto.SocialJoinRequest;
import jparest.practice.user.service.UserAuthService;
import org.springframework.beans.factory.annotation.Autowired;

// 서비스 테스트 시 회원가입을 하는 목 클래스
public class MockUserJoin {

    @Autowired
    UserAuthService userAuthService;

    protected final String socialUserId1 = "123123";
    protected final String email1 = "eee@www.aaaa";
    protected final String nickname1 = "유저1";
    protected final LoginType loginType1 = LoginType.KAKAO;

    protected final String socialUserId2 = "234234";
    protected final String email2 = "eee@www.bbb";
    protected final String nickname2 = "유저2";
    protected final LoginType loginType2 = LoginType.KAKAO;

    protected User joinUser1;
    protected User joinUser2;

    protected void joinSetUp() {
        this.joinUser1 = join(new SocialJoinRequest(socialUserId1, email1, nickname1, loginType1));
        this.joinUser2 = join(new SocialJoinRequest(socialUserId2, email2, nickname2, loginType2));
    }

    private User join(SocialJoinRequest socialJoinRequest) {
        return userAuthService.join(socialJoinRequest);
    }
}
