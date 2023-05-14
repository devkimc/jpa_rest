package jparest.practice.user.service;

import jparest.practice.user.domain.User;
import jparest.practice.user.dto.KakaoLoginResponse;
import jparest.practice.user.dto.UserLoginResponse;

public interface UserAuthService {
    User join(User user);

//    UserLoginResponse login(String loginId, String password);

    KakaoLoginResponse kakaoLogin(String code);
}
