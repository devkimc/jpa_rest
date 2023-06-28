package jparest.practice.user.controller;

import jparest.practice.auth.jwt.JwtService;
import jparest.practice.common.document.RestDocsTestSupport;
import jparest.practice.common.fixture.UserFixture;
import jparest.practice.user.dto.UpdateUserInfoRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.patch;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc(addFilters = false)
public class UserInfoControllerTest extends RestDocsTestSupport {

    private final String USER_API = "/api/user";

    @MockBean
    JwtService jwtService;

    @Test
    @DisplayName("닉네임 변경")
    public void get_nickname_duplicate() throws Exception {

        //given
        given(userInfoService.updateUserInfo(any(), any())).willReturn(true);

        UpdateUserInfoRequest updateUserInfoRequest = UpdateUserInfoRequest.builder()
                .nickname(UserFixture.nickname2)
                .build();

        //when
        ResultActions result = mockMvc.perform(
                patch(USER_API + "/info")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createJson(updateUserInfoRequest))
        );

        //then
        result
                .andExpect(status().isOk())
                .andExpectAll(
                        jsonPath("$.result").value(true)
                )
                .andDo(restDocs.document(
                        requestFields(
                                fieldWithPath("nickname").description("닉네임")
                        ),
                        responseFields(
                                fieldWithPath("success").description("성공 여부"),
                                fieldWithPath("result").description("성공 여부")
                        )
                ));

    }
}
