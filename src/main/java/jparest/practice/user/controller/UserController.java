package jparest.practice.user.controller;

import jparest.practice.auth.jwt.TokenType;
import jparest.practice.common.util.ApiResult;
import jparest.practice.common.util.ApiUtils;
import jparest.practice.user.domain.User;
import jparest.practice.user.dto.UserLoginRequest;
import jparest.practice.user.dto.UserLoginResponse;
import jparest.practice.user.service.UserService;
import jparest.practice.user.dto.UserInfoResponse;
import jparest.practice.common.util.CookieUtils;
import jparest.practice.common.util.TokenDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@CrossOrigin("*")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {
    @Value("${domain.host}")
    private String domain;

    private final UserService userService;

    @PostMapping(name = "로그인", value = "/login")
    public ApiResult<UserLoginResponse> login(@RequestBody UserLoginRequest userLoginRequest, HttpServletResponse response) {
        UserLoginResponse userLoginResponse  = userService.login(userLoginRequest.getLoginId(), userLoginRequest.getPassword());

        if (userLoginResponse.getTokenDto() != null) {
            TokenDto tokenDto = userLoginResponse.getTokenDto();

            CookieUtils.addCookie(response, TokenType.ACCESS_TOKEN.name(), tokenDto.getAccessToken(), domain);
            CookieUtils.addCookie(response, TokenType.REFRESH_TOKEN.name(), tokenDto.getRefreshToken(), domain);

            return ApiUtils.success(new UserLoginResponse(userLoginResponse.getEmail(), userLoginResponse.getName()));
        }

        return ApiUtils.fail(userLoginResponse);
    }

    @PostMapping(name = "회원가입", value = "/join")
    public ApiResult<Boolean> join(@RequestBody User user) {
        userService.join(user);
        return ApiUtils.success(Boolean.TRUE);
    }

    @GetMapping(name = "회원 정보조회", value = "/user/{id}")
    public ApiResult<UserInfoResponse> info(@PathVariable long id) {
        UserInfoResponse userInfoResponse = userService.getInfo(id);
        return ApiUtils.success(userInfoResponse);
    }

}
