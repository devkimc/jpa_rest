package jparest.practice.controller;

import jparest.practice.common.utils.RestDocsTestSupport;
import jparest.practice.invite.dto.GetWaitingInviteResponse;
import jparest.practice.invite.dto.InviteUserRequest;
import jparest.practice.invite.dto.InviteUserResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.List;

import static jparest.practice.common.utils.fixture.GroupFixture.groupName1;
import static jparest.practice.common.utils.fixture.UserFixture.nickname1;
import static jparest.practice.common.utils.fixture.UserFixture.userId;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc(addFilters = false)
public class InviteControllerTest extends RestDocsTestSupport {

    private final String INVITE_API = "/api/invites";

    @Test
    @DisplayName("유저 초대")
    void add_invites() throws Exception {

        //given
        InviteUserRequest inviteUserRequest = new InviteUserRequest(userId, 1L);

        given(inviteService.inviteToGroup(any(), any()))
                .willReturn(new InviteUserResponse(1L));

        //when
        ResultActions result = mockMvc.perform(
                post(INVITE_API)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createJson(inviteUserRequest))
        );

        //then
        result
                .andExpect(status().isOk())
                .andExpectAll(
                        jsonPath("$.result.inviteId").exists()
                )
                .andDo(restDocs.document(
                        responseFields(
                                fieldWithPath("success").description("성공 여부"),
                                fieldWithPath("result.inviteId").description("초대 아이디")
                        )));
    }

    @Test
    @DisplayName("초대받은 리스트 조회")
    void get_invites() throws Exception {

        //given
        InviteUserRequest inviteUserRequest = new InviteUserRequest(userId, 1L);

        List<GetWaitingInviteResponse> response = new ArrayList<>();

        response.add(GetWaitingInviteResponse.builder()
                .inviteId(1L)
                .nickName(nickname1)
                .groupName(groupName1)
                .build());

        given(inviteService.getWaitingInviteList( any()))
                .willReturn(response);

        //when
        ResultActions result = mockMvc.perform(
                get(INVITE_API)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createJson(inviteUserRequest))
        );

        //then
        result
                .andExpect(status().isOk())
                .andExpectAll(
                        jsonPath("$.result.[0].inviteId").value(1L),
                        jsonPath("$.result.[0].nickName").value(nickname1),
                        jsonPath("$.result.[0].groupName").value(groupName1)
                )
                .andDo(restDocs.document(
                        responseFields(
                                fieldWithPath("success").description("성공 여부"),
                                fieldWithPath("result.[].inviteId").description("초대 아이디"),
                                fieldWithPath("result.[].nickName").description("초대한 사람의 닉네임"),
                                fieldWithPath("result.[].groupName").description("초대 그룹 이름")
                        )));
    }
}
