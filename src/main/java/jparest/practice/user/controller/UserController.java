package jparest.practice.user.controller;

import jparest.practice.auth.jwt.TokenType;
import jparest.practice.auth.security.CurrentUser;
import jparest.practice.common.util.ApiResult;
import jparest.practice.common.util.ApiUtils;
import jparest.practice.common.util.CookieUtils;
import jparest.practice.common.util.TokenDto;
import jparest.practice.user.domain.User;
import jparest.practice.user.dto.SocialJoinRequest;
import jparest.practice.user.dto.SocialJoinResponse;
import jparest.practice.user.dto.SocialLoginResponse;
import jparest.practice.user.service.UserAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@CrossOrigin("*")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class UserController {
    @Value("${domain.host}")
    private String domain;

    private final UserAuthService userAuthService;

    @PostMapping(value = "/join")
    public ApiResult<SocialJoinResponse> join(@RequestBody SocialJoinRequest socialJoinRequest) {
        SocialJoinResponse socialJoinResponse = userAuthService.socialJoin(socialJoinRequest);
        return ApiUtils.success(socialJoinResponse);
    }

    @PostMapping("/kakao")
    public ApiResult<SocialLoginResponse> kakaoLogin(@RequestParam("code") String code, HttpServletResponse response) {
        SocialLoginResponse socialLoginResponse = userAuthService.kakaoLogin(code);

        if (socialLoginResponse.getTokenDto() != null) {
            TokenDto tokenDto = socialLoginResponse.getTokenDto();

            CookieUtils.addCookie(response, TokenType.ACCESS_TOKEN.name(), tokenDto.getAccessToken(), domain);
            CookieUtils.addCookie(response, TokenType.REFRESH_TOKEN.name(), tokenDto.getRefreshToken(), domain);

            return ApiUtils.success(socialLoginResponse);
        }

        return ApiUtils.fail(socialLoginResponse);
    }

    @DeleteMapping("/logout")
    public ApiResult<Boolean> logout(HttpServletRequest request, HttpServletResponse response,
                                     @CurrentUser User user) {
        userAuthService.logout(request, response, String.valueOf(user.getId()));
        return ApiUtils.success(Boolean.TRUE);
    }

//    @PostMapping(name = "회원가입", value = "/join")
//    public ApiResult<Boolean> join(@RequestBody User user) {
//        userAuthService.join(user);
//        return ApiUtils.success(Boolean.TRUE);
//    }

// 아이디, 비밀번호 로그인 시
//    @GetMapping(name = "회원 정보조회", value = "/{id}")
//    public ApiResult<UserInfoResponse> info(@PathVariable long id) {
//        UserInfoResponse userInfoResponse = userAuthServiceImpl.getInfo(id);
//        return ApiUtils.success(userInfoResponse);환
//    }

//    @PostMapping(name = "로그인", value = "/login")
//    public ApiResult<UserLoginResponse> login(@RequestBody UserLoginRequest userLoginRequest, HttpServletResponse response) {
//        UserLoginResponse userLoginResponse = userAuthService.login(userLoginRequest.getLoginId(), userLoginRequest.getPassword());
//
//        if (userLoginResponse.getTokenDto() != null) {
//            TokenDto tokenDto = userLoginResponse.getTokenDto();
//
//            CookieUtils.addCookie(response, TokenType.ACCESS_TOKEN.name(), tokenDto.getAccessToken(), domain);
//            CookieUtils.addCookie(response, TokenType.REFRESH_TOKEN.name(), tokenDto.getRefreshToken(), domain);
//
//            return ApiUtils.success(new UserLoginResponse(userLoginResponse.getEmail(), userLoginResponse.getName()));
//        }
//
//        return ApiUtils.fail(userLoginResponse);
//    }

}
