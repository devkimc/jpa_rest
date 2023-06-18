package jparest.practice.user.service;

import jparest.practice.user.domain.User;
import jparest.practice.user.dto.KakaoLoginResponse;
import jparest.practice.user.dto.SocialJoinRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface UserAuthService {

    // 카카오 로그인
    KakaoLoginResponse kakaoLogin(String code);

    // 로그아웃
    Boolean logout(HttpServletRequest request, HttpServletResponse response, User user);

    // 테스트용 회원가입
    User join(SocialJoinRequest socialJoinRequest);
}
