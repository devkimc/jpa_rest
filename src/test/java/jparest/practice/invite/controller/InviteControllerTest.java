package jparest.practice.invite.controller;

import jparest.practice.common.document.RestDocsTestSupport;
import jparest.practice.invite.domain.InviteStatus;
import jparest.practice.invite.dto.GetWaitingInviteResponse;
import jparest.practice.invite.dto.InviteStatusPatchRequest;
import jparest.practice.invite.dto.InviteUserRequest;
import jparest.practice.invite.dto.InviteUserResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static jparest.practice.common.fixture.GroupFixture.groupName1;
import static jparest.practice.common.fixture.UserFixture.nickname1;
import static jparest.practice.common.fixture.UserFixture.userId;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
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
                        requestFields(
                                fieldWithPath("recvUserId").description("초대할 유저 아이디"),
                                fieldWithPath("groupId").description("그룹 아이디")
                        ),
                        responseFields(
                                fieldWithPath("success").description("성공 여부"),
                                fieldWithPath("result.inviteId").description("초대 아이디")
                        )));
    }

    @Test
    @DisplayName("초대 상태 변경")
    void update_invites_status() throws Exception {

        //given
        InviteStatusPatchRequest inviteStatusPatchRequest = new InviteStatusPatchRequest(InviteStatus.ACCEPT);

        given(inviteService.procInvitation(any(), any(), any()))
                .willReturn(true);

        //when
        ResultActions result = mockMvc.perform(
                patch(INVITE_API + "/{inviteId}/status", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createJson(inviteStatusPatchRequest))
        );

        //then
        result
                .andExpect(status().isOk())
                .andExpectAll(
                        jsonPath("$.result").value(true)
                )
                .andDo(restDocs.document(
                        pathParameters(
                                parameterWithName("inviteId").description("초대 아이디")
                        ),
                        requestFields(
                                fieldWithPath("inviteStatus").description(inviteStatus)
                        ),
                        responseFields(
                                fieldWithPath("success").description("성공 여부"),
                                fieldWithPath("result").description("성공 여부")
                        )));
    }

    @Test
    @DisplayName("초대받은 리스트 조회")
    void get_invites() throws Exception {

        //given
        List<GetWaitingInviteResponse> response = new ArrayList<>();

        response.add(GetWaitingInviteResponse.builder()
                .inviteId(1L)
                .nickName(nickname1)
                .groupName(groupName1)
                .build());

        given(inviteService.getWaitingInviteList(any()))
                .willReturn(response);

        //when
        ResultActions result = mockMvc.perform(
                get(INVITE_API)
                        .contentType(MediaType.APPLICATION_JSON)
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

    // 추후 열거형 함수 처리용
    String inviteStatus = Arrays.stream(InviteStatus.values())
            .map(type -> String.format("`%s`", type))
            .collect(Collectors.joining(", "));
}
