package jparest.practice.auth.user.ui;

import jparest.practice.auth.jwt.TokenType;
import jparest.practice.auth.security.dto.LoginDTO;
import jparest.practice.auth.user.application.UserService;
import jparest.practice.auth.user.application.dto.UserInfoDTO;
import jparest.practice.auth.user.application.dto.UserJoinDTO;
import jparest.practice.common.ApiResponse;
import jparest.practice.common.DataApiResponse;
import jparest.practice.common.util.CookieUtils;
import jparest.practice.common.util.TokenDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@CrossOrigin("*")
@RestController
@RequiredArgsConstructor
public class UserController {
    @Value("${domain.host}")
    private String domain;

    private final UserService userService;

    @PostMapping(name = "로그인", value = "/login")
    public ApiResponse login(@RequestBody LoginDTO loginDTO, HttpServletResponse response) {
        TokenDto tokenDto = userService.login(loginDTO);
        if (tokenDto != null) {
            CookieUtils.addCookie(response, TokenType.ACCESS_TOKEN.name(), tokenDto.getAccessToken(), domain);
            CookieUtils.addCookie(response, TokenType.REFRESH_TOKEN.name(), tokenDto.getRefreshToken(), domain);
        }

        return new ApiResponse();
    }

    @PostMapping(name = "회원가입", value = "/user")
    public ApiResponse join(@RequestBody UserJoinDTO userJoinDTO) {
        userService.join(userJoinDTO);
        return new ApiResponse();
    }

    @GetMapping(name = "회원 정보조회", value = "/user/{id}")
    public DataApiResponse<UserInfoDTO> info(@PathVariable long id) {
        UserInfoDTO userInfoDTO = userService.getInfo(id);
        return new DataApiResponse<>(userInfoDTO);
    }

}
