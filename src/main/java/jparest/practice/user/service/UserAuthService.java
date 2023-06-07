package jparest.practice.user.service;

import jparest.practice.user.domain.User;
import jparest.practice.user.dto.KakaoLoginResponse;
import jparest.practice.user.dto.SocialJoinRequest;
import jparest.practice.user.dto.SocialJoinResponse;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface UserAuthService {
    User join(SocialJoinRequest socialJoinRequest);
    SocialJoinResponse socialJoin(SocialJoinRequest socialJoinRequest);

    Boolean logout(HttpServletRequest request, HttpServletResponse response, User user);

    KakaoLoginResponse kakaoLogin(String code);

    User testKakaoLogin(SocialJoinRequest socialJoinRequest);
}
