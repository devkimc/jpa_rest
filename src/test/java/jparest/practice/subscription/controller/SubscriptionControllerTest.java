package jparest.practice.subscription.controller;

import jparest.practice.common.document.RestDocsTestSupport;
import jparest.practice.common.fixture.SubscriptionFixture;
import jparest.practice.subscription.domain.SubscriptionStatus;
import jparest.practice.subscription.dto.ProcessSubscriptionRequest;
import jparest.practice.subscription.dto.SubscribeForGroupRequest;
import jparest.practice.subscription.dto.SubscribeForGroupResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Arrays;
import java.util.stream.Collectors;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.patch;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
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
                .message(SubscriptionFixture.message1)
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

    // 추후 열거형 함수 처리용
    String subscriptionStatus = Arrays.stream(SubscriptionStatus.values())
            .map(type -> String.format("`%s`", type))
            .collect(Collectors.joining(", "));

}