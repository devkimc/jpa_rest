package jparest.practice.user.service;

import jparest.practice.user.domain.User;
import jparest.practice.user.dto.SocialJoinRequest;
import jparest.practice.user.dto.SocialJoinResponse;
import jparest.practice.user.dto.SocialLoginResponse;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface UserAuthService {
    User join(SocialJoinRequest socialJoinRequest);
    SocialJoinResponse socialJoin(SocialJoinRequest socialJoinRequest);

    void logout(HttpServletRequest request, HttpServletResponse response, String userId);

    SocialLoginResponse kakaoLogin(String code);

    User testKakaoLogin(SocialJoinRequest socialJoinRequest);
}
