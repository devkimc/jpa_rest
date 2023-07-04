package jparest.practice.subscription.controller;

import jparest.practice.common.document.RestDocsTestSupport;
import jparest.practice.common.fixture.SubscriptionFixture;
import jparest.practice.subscription.dto.SubscribeForGroupRequest;
import jparest.practice.subscription.dto.SubscribeForGroupResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
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
                .message(SubscriptionFixture.message)
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

}