package jparest.practice.user.controller;

import jparest.practice.auth.jwt.TokenType;
import jparest.practice.auth.security.CurrentUser;
import jparest.practice.common.util.ApiResult;
import jparest.practice.common.util.ApiUtils;
import jparest.practice.common.util.CookieUtils;
import jparest.practice.common.util.TokenDto;
import jparest.practice.user.domain.User;
import jparest.practice.user.dto.KakaoLoginResponse;
import jparest.practice.user.dto.SocialLoginResponse;
import jparest.practice.user.service.UserAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class UserController {
    
    @Value("${domain.host}")
    private String domain;

    private final UserAuthService userAuthService;

    @PostMapping("/kakao")
    public ApiResult<SocialLoginResponse> kakaoLogin(@RequestParam("code") String code, HttpServletResponse response) {
        KakaoLoginResponse kakaoLoginResponse = userAuthService.kakaoLogin(code);

        SocialLoginResponse socialLoginResponse = SocialLoginResponse.builder()
                .socialUserId(kakaoLoginResponse.getSocialUserId())
                .email(kakaoLoginResponse.getEmail())
                .nickname(kakaoLoginResponse.getNickname())
                .loginType(kakaoLoginResponse.getLoginType())
                .build();

        if (kakaoLoginResponse.getTokenDto() != null) {
            TokenDto tokenDto = kakaoLoginResponse.getTokenDto();

            CookieUtils.addCookie(response, TokenType.ACCESS_TOKEN.name(), tokenDto.getAccessToken(), domain);
            CookieUtils.addCookie(response, TokenType.REFRESH_TOKEN.name(), tokenDto.getRefreshToken(), domain);

            return ApiUtils.success(socialLoginResponse);
        }

        return ApiUtils.fail(socialLoginResponse);
    }

    @DeleteMapping("/logout")
    public ApiResult<Boolean> logout(HttpServletRequest request, HttpServletResponse response,
                                     @CurrentUser User user) {
        userAuthService.logout(request, response, user);
        return ApiUtils.success(Boolean.TRUE);
    }

}
