package jparest.practice.member.controller;

import jparest.practice.auth.jwt.TokenType;
import jparest.practice.common.util.ApiResult;
import jparest.practice.common.util.ApiUtils;
import jparest.practice.member.dto.UserLoginRequest;
import jparest.practice.member.dto.UserLoginResponse;
import jparest.practice.member.service.MemberService;
import jparest.practice.member.dto.UserInfoResponse;
import jparest.practice.member.dto.UserJoinRequest;
import jparest.practice.common.util.CookieUtils;
import jparest.practice.common.util.TokenDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@CrossOrigin("*")
@RestController
@RequiredArgsConstructor
public class MemberController {
    @Value("${domain.host}")
    private String domain;

    private final MemberService memberService;

    @PostMapping(name = "로그인", value = "/login")
    public ApiResult<UserLoginResponse> login(@RequestBody UserLoginRequest userLoginRequest, HttpServletResponse response) {
        UserLoginResponse userLoginResponse  = memberService.login(userLoginRequest);

        if (userLoginResponse.getTokenDto() != null) {
            TokenDto tokenDto = userLoginResponse.getTokenDto();

            CookieUtils.addCookie(response, TokenType.ACCESS_TOKEN.name(), tokenDto.getAccessToken(), domain);
            CookieUtils.addCookie(response, TokenType.REFRESH_TOKEN.name(), tokenDto.getRefreshToken(), domain);

            return ApiUtils.success(new UserLoginResponse(userLoginResponse.getEmail(), userLoginResponse.getName()));
        }

        return ApiUtils.fail(userLoginResponse);
    }

    @PostMapping(name = "회원가입", value = "/user")
    public ApiResult<Boolean> join(@RequestBody UserJoinRequest userJoinRequest) {
        memberService.join(userJoinRequest);
        return ApiUtils.success(Boolean.TRUE);
    }

    @GetMapping(name = "회원 정보조회", value = "/user/{id}")
    public ApiResult<UserInfoResponse> info(@PathVariable long id) {
        UserInfoResponse userInfoResponse = memberService.getInfo(id);
        return ApiUtils.success(userInfoResponse);
    }

}
