package jparest.practice.member.controller;

import jparest.practice.auth.jwt.TokenType;
import jparest.practice.member.dto.LoginDTO;
import jparest.practice.member.service.MemberService;
import jparest.practice.member.dto.MemberInfoDTO;
import jparest.practice.member.dto.MemberJoinDTO;
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
public class MemberController {
    @Value("${domain.host}")
    private String domain;

    private final MemberService memberService;

    @PostMapping(name = "로그인", value = "/login")
    public ApiResponse login(@RequestBody LoginDTO loginDTO, HttpServletResponse response) {
        TokenDto tokenDto = memberService.login(loginDTO);
        if (tokenDto != null) {
            CookieUtils.addCookie(response, TokenType.ACCESS_TOKEN.name(), tokenDto.getAccessToken(), domain);
            CookieUtils.addCookie(response, TokenType.REFRESH_TOKEN.name(), tokenDto.getRefreshToken(), domain);
        }

        return new ApiResponse();
    }

    @PostMapping(name = "회원가입", value = "/user")
    public ApiResponse join(@RequestBody MemberJoinDTO memberJoinDTO) {
        memberService.join(memberJoinDTO);
        return new ApiResponse();
    }

    @GetMapping(name = "회원 정보조회", value = "/user/{id}")
    public DataApiResponse<MemberInfoDTO> info(@PathVariable long id) {
        MemberInfoDTO memberInfoDto = memberService.getInfo(id);
        return new DataApiResponse<>(memberInfoDto);
    }

}
