package jparest.practice.user.controller;

import jparest.practice.auth.jwt.JwtService;
import jparest.practice.common.utils.RestDocsTestSupport;
import jparest.practice.user.dto.KakaoLoginResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import static jparest.practice.common.utils.fixture.UserFixture.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc(addFilters = false)
public class UserAuthControllerTest extends RestDocsTestSupport {

    private final String AUTH_API = "/api/auth";

    @MockBean
    JwtService jwtService;

    @Test
    @DisplayName("카카오 로그인 및 회원가입")
    public void login_kakao() throws Exception {

        //given
        MultiValueMap<String, String> params = new LinkedMultiValueMap();
        params.add("code", "dCBtBeJy0NzZhRzVxqptBg4jvxgD11zsPsXhPGXx-Wy05qVNK0if0sFwqA7OTglVJjtVdwoqJQ4AAAGIlPJfEQ");

        KakaoLoginResponse kakaoLoginResponse = KakaoLoginResponse.builder()
                .socialUserId(socialUserId1)
                .email(email1)
                .nickname(nickname1)
                .loginType(loginType1)
                .build();

        given(userAuthService.kakaoLogin(any())).willReturn(kakaoLoginResponse);

        //when
        ResultActions result = mockMvc.perform(
                post(AUTH_API + "/kakao")
                        .contentType(MediaType.APPLICATION_JSON)
                        .params(params)
        );

        //then
        result
                .andExpect(status().isOk())
                .andExpectAll(
                        jsonPath("$.result.socialUserId").value(socialUserId1),
                        jsonPath("$.result.email").value(email1),
                        jsonPath("$.result.nickname").value(nickname1),
                        jsonPath("$.result.loginType").value(loginType1.name())
                )
                .andDo(restDocs.document(
                        requestParameters(
                                parameterWithName("code").description("카카오 로그인 후 제공받은 코드")
                        ),
                        responseFields(
                                fieldWithPath("success").description("성공 여부"),
                                fieldWithPath("result.socialUserId").description("소셜 유저 아이디"),
                                fieldWithPath("result.email").description("유저 이메일"),
                                fieldWithPath("result.nickname").description("닉네임"),
                                fieldWithPath("result.loginType").description("로그인 타입")
                        )
                ));
    }

    @Test
    @DisplayName("로그아웃")
    public void logout() throws Exception {

        //given
        given(userAuthService.logout(any(),any(),any())).willReturn(true);

        //when
        ResultActions result = mockMvc.perform(
                delete(AUTH_API + "/logout")
                        .contentType(MediaType.APPLICATION_JSON)
        );

        //then
        result
                .andExpect(status().isOk())
                .andExpectAll(
                        jsonPath("$.result").value(true)
                )
                .andDo(restDocs.document(
                        responseFields(
                                fieldWithPath("success").description("성공 여부"),
                                fieldWithPath("result").description("성공 여부")
                        )
                ));
    }
}
