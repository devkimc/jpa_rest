package jparest.practice.rest.controller;

import jparest.practice.common.utils.RestDocsTestSupport;
import jparest.practice.rest.dto.GetMostSavedRestResponse;
import jparest.practice.rest.dto.GetNewSavedRestResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static jparest.practice.common.utils.fixture.RestFixture.restIdList;
import static jparest.practice.common.utils.fixture.RestFixture.restNameList;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class RankingRestaurantControllerTest extends RestDocsTestSupport {

    private final String REST_RANKING_API = "/api/restaurants/ranking";

    @Test
    @DisplayName("가장 많이 저장된 맛집 조회")
    public void get_fav_rests_ranking_most_save() throws Exception {

        //given
        List<GetMostSavedRestResponse> response = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            GetMostSavedRestResponse rest = GetMostSavedRestResponse.builder()
                    .rank(i + 1)
                    .restId(restIdList.get(i))
                    .restName(restNameList.get(i))
                    .totalFavorite(5 - i)
                    .build();

            response.add(rest);
        }

        given(rankingRestaurantService.getMostSavedRest()).willReturn(response);

        //when
        ResultActions result = mockMvc.perform(
                RestDocumentationRequestBuilders.get(REST_RANKING_API + "/most/save")
                        .contentType(MediaType.APPLICATION_JSON)
        );

        //then
        result
                .andExpect(status().isOk())
                .andExpectAll(
                        jsonPath("$.result.[0].rank").value(1),
                        jsonPath("$.result.[0].restId").exists(),
                        jsonPath("$.result.[0].restName").exists(),
                        jsonPath("$.result.[0].totalFavorite").exists()
                )
                .andDo(restDocs.document(
                        responseFields(
                                fieldWithPath("success").description("성공 여부"),
                                fieldWithPath("result.[].rank").description("순위"),
                                fieldWithPath("result.[].restId").description("식당 아이디"),
                                fieldWithPath("result.[].restName").description("식당 이름"),
                                fieldWithPath("result.[].totalFavorite").description("맛집 저장 횟수")
                        )
                ))
        ;
    }

    @Test
    @DisplayName("가장 최근에 저장된 맛집 조회")
    public void get_fav_rests_ranking_new_save() throws Exception {

        //given
        List<GetNewSavedRestResponse> response = new ArrayList<>();

        LocalDateTime now = LocalDateTime.now();

        for (int i = 0; i < 5; i++) {
            GetNewSavedRestResponse rest = GetNewSavedRestResponse.builder()
                    .rank(i + 1)
                    .restId(restIdList.get(i))
                    .restName(restNameList.get(i))
                    .savedAt(now)
                    .build();

            response.add(rest);
        }

        given(rankingRestaurantService.getNewSavedRest()).willReturn(response);

        //when
        ResultActions result = mockMvc.perform(
                RestDocumentationRequestBuilders.get(REST_RANKING_API + "/new/save")
                        .contentType(MediaType.APPLICATION_JSON)
        );

        //then
        result
                .andExpect(status().isOk())
                .andExpectAll(
                        jsonPath("$.result.[0].rank").value(1),
                        jsonPath("$.result.[0].restId").exists(),
                        jsonPath("$.result.[0].restName").exists(),
                        jsonPath("$.result.[0].savedAt").exists()
                )
                .andDo(restDocs.document(
                        responseFields(
                                fieldWithPath("success").description("성공 여부"),
                                fieldWithPath("result.[].rank").description("순위"),
                                fieldWithPath("result.[].restId").description("식당 아이디"),
                                fieldWithPath("result.[].restName").description("식당 이름"),
                                fieldWithPath("result.[].savedAt").description("맛집 저장 시간")
                        )
                ))
        ;
    }
}