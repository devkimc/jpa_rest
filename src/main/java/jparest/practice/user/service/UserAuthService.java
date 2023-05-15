package jparest.practice.user.service;

import jparest.practice.user.domain.User;
import jparest.practice.user.dto.SocialJoinRequest;
import jparest.practice.user.dto.SocialJoinResponse;
import jparest.practice.user.dto.SocialLoginResponse;

public interface UserAuthService {
    User join(User user);

    SocialJoinResponse socialJoin(SocialJoinRequest socialJoinRequest);

//    UserLoginResponse login(String loginId, String password);

    SocialLoginResponse kakaoLogin(String code);
}
