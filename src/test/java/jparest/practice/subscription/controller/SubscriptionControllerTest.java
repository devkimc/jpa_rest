package jparest.practice.subscription.controller;

import jparest.practice.common.document.RestDocsTestSupport;
import jparest.practice.subscription.domain.SubscriptionStatus;
import jparest.practice.subscription.dto.GetReceivedSubscriptionResponse;
import jparest.practice.subscription.dto.ProcessSubscriptionRequest;
import jparest.practice.subscription.dto.SubscribeForGroupRequest;
import jparest.practice.subscription.dto.SubscribeForGroupResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static jparest.practice.common.fixture.SubscriptionFixture.message1;
import static jparest.practice.common.fixture.SubscriptionFixture.message2;
import static jparest.practice.common.fixture.UserFixture.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc(addFilters = false)
public class SubscriptionControllerTest extends RestDocsTestSupport {

    private final String SUBSCRIPTION_API = "/api/subscription";

    @Test
    @DisplayName("그룹 가입 신청")
    public void subscribe_for_group() throws Exception {

        //given
        SubscribeForGroupRequest request = SubscribeForGroupRequest.builder()
                .groupId(1L)
                .message(message1)
                .build();

        SubscribeForGroupResponse response = SubscribeForGroupResponse.builder()
                .subscriptionId(1L)
                .build();

        given(subscriptionService.subscribeForGroup(any(), any())).willReturn(response);

        //when
        ResultActions result = mockMvc.perform(
                post(SUBSCRIPTION_API)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        );

        //then
        result
                .andExpect(status().isOk())
                .andExpectAll(
                        jsonPath("$.result.subscriptionId").value(1L)
                )
                .andDo(restDocs.document(
                        requestFields(
                                fieldWithPath("groupId").description("그룹 아이디"),
                                fieldWithPath("message").description("가입 신청 메시지")
                        ),
                        responseFields(
                                fieldWithPath("success").description("성공 여부"),
                                fieldWithPath("result.subscriptionId").description("가입 신청 아이디")
                        )
                ));
    }

    @Test
    @DisplayName("가입 신청 상태 처리")
    void update_subscription_status() throws Exception {

        //given
        ProcessSubscriptionRequest processSubscriptionRequest = new ProcessSubscriptionRequest(
                SubscriptionStatus.ACCEPT);

        given(subscriptionService.processSubscription(any(), any(), any()))
                .willReturn(true);

        //when
        ResultActions result = mockMvc.perform(
                patch(SUBSCRIPTION_API + "/{subscriptionId}/status", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createJson(processSubscriptionRequest))
        );

        //then
        result
                .andExpect(status().isOk())
                .andExpectAll(
                        jsonPath("$.result").value(true)
                )
                .andDo(restDocs.document(
                        pathParameters(
                                parameterWithName("subscriptionId").description("가입신청 아이디")
                        ),
                        requestFields(
                                fieldWithPath("status").description(subscriptionStatus)
                        ),
                        responseFields(
                                fieldWithPath("success").description("성공 여부"),
                                fieldWithPath("result").description("성공 여부")
                        )));
    }

    @Test
    @DisplayName("가입 신청한 유저 조회")
    void get_subscription_users() throws Exception {

        //given
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.set("groupId", "1");

        List<GetReceivedSubscriptionResponse> response = new ArrayList<>();

        GetReceivedSubscriptionResponse response1 = GetReceivedSubscriptionResponse.builder()
                .subscriptionId(1L)
                .applicantId(userId1)
                .applicantNickname(nickname1)
                .message(message1)
                .createdAt(LocalDateTime.now())
                .build();

        GetReceivedSubscriptionResponse response2 = GetReceivedSubscriptionResponse.builder()
                .subscriptionId(2L)
                .applicantId(userId2)
                .applicantNickname(nickname2)
                .message(message2)
                .createdAt(LocalDateTime.now())
                .build();

        response.add(response1);
        response.add(response2);

        given(subscriptionService.getReceivedSubscription(any(), any()))
                .willReturn(response);

        //when
        ResultActions result = mockMvc.perform(
                get(SUBSCRIPTION_API + "/users", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .params(params)
        );

        //then
        result
                .andExpect(status().isOk())
                .andExpectAll(
                        jsonPath("$.result").isArray(),
                        jsonPath("$.result.[0].applicantNickname").value(nickname1),
                        jsonPath("$.result.[1].applicantNickname").value(nickname2)
                )
                .andDo(restDocs.document(
                        requestParameters(
                                parameterWithName("groupId").description("그룹 아이디")
                        ),
                        responseFields(
                                fieldWithPath("success").description("성공 여부"),
                                fieldWithPath("result.[].subscriptionId").description("가입 신청 아이디"),
                                fieldWithPath("result.[].applicantId").description("가입 신청자 아이디"),
                                fieldWithPath("result.[].applicantNickname").description("가입 신청자 닉네임"),
                                fieldWithPath("result.[].message").description("가입 신청 메시지"),
                                fieldWithPath("result.[].createdAt").description("신청 시간")
                        )));
    }

    // 추후 열거형 함수 처리용
    String subscriptionStatus = Arrays.stream(SubscriptionStatus.values())
            .map(type -> String.format("`%s`", type))
            .collect(Collectors.joining(", "));
}