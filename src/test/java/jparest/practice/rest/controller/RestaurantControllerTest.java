package jparest.practice.rest.controller;

import jparest.practice.common.utils.RestDocsTestSupport;
import jparest.practice.rest.dto.AddFavoriteRestRequest;
import jparest.practice.rest.dto.GetFavRestListResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.List;

import static jparest.practice.common.utils.fixture.RestFixture.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc(addFilters = false)
public class RestaurantControllerTest extends RestDocsTestSupport {

    private final String RESTAURANT_API = "/api/restaurants";

    @Test
    @DisplayName("식당 이름이 빈값일 경우 에러를 반환한다.")
    public void chk_add_favorite_rests_validation() throws Exception {

        //given
        AddFavoriteRestRequest requestBody = AddFavoriteRestRequest.builder()
                .groupId(1L)
                .restName("")
                .latitude(latitude)
                .longitude(longitude)
                .build();

        given(restService.addFavRest(any(), any(), any()))
                .willReturn(true);

        //when
        ResultActions result = mockMvc.perform(
                post(RESTAURANT_API + "/{restId}/favorite", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createJson(requestBody))
        );

        //then
        result
                .andExpect(status().isBadRequest())
                .andExpectAll(
                        jsonPath("$.field").value("restName")
                );
    }


    @Test
    @DisplayName("맛집 추가")
    void add_favorite_rests() throws Exception {

        //given
        AddFavoriteRestRequest requestBody = AddFavoriteRestRequest.builder()
                .groupId(1L)
                .restName(restName)
                .latitude(latitude)
                .longitude(longitude)
                .build();

        given(restService.addFavRest(any(), any(), any()))
                .willReturn(true);

        //when
        ResultActions result = mockMvc.perform(
                post(RESTAURANT_API + "/{restId}/favorite", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createJson(requestBody))
        );

        //then
        result
                .andExpect(status().isOk())
                .andExpectAll(
                        jsonPath("$.result").value(true)
                )
                .andDo(restDocs.document(
                        pathParameters(
                                parameterWithName("restId").description("식당 아이디")
                        ),
                        responseFields(
                                fieldWithPath("success").description("성공 여부"),
                                fieldWithPath("result").description("성공 여부")
                        )));
    }

    @Test
    @DisplayName("맛집 삭제")
    void delete_favorite_rests() throws Exception {

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("groupId", "1");

        //given
        given(restService.deleteFavRest(any(), any(), any()))
                .willReturn(true);

        //when
        ResultActions result = mockMvc.perform(
                delete(RESTAURANT_API + "/{restId}/favorite", 1L)
                        .params(params)
                        .contentType(MediaType.APPLICATION_JSON)
        );

        //then
        result
                .andExpect(status().isOk())
                .andExpectAll(
                        jsonPath("$.result").value(true)
                )
                .andDo(restDocs.document(
                        requestParameters(
                                parameterWithName("groupId").description("그룹 아이디")
                        ),
                        pathParameters(
                                parameterWithName("restId").description("식당 아이디")
                        ),
                        responseFields(
                                fieldWithPath("success").description("성공 여부"),
                                fieldWithPath("result").description("성공 여부")
                        )));
    }

    @Test
    @DisplayName("맛집 리스트 조회")
    void get_favorite_rests() throws Exception {

        //given
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("groupId", "1");

        GetFavRestListResponse favRest = GetFavRestListResponse.builder()
                .restId(restId)
                .restName(restName)
                .latitude(latitude)
                .longitude(longitude)
                .build();

        PageImpl<GetFavRestListResponse> response = new PageImpl<>(List.of(favRest), PageRequest.of(0, 10), 1);

        given(restService.getFavRestList(any(), any()))
                .willReturn(response);

        //when
        ResultActions result = mockMvc.perform(
                get(RESTAURANT_API + "/favorite")
                        .params(params)
                        .contentType(MediaType.APPLICATION_JSON)
        );

        //then
        result
                .andExpect(status().isOk())
                .andExpectAll(
                        jsonPath("$.result.content.[0].restId").value(restId),
                        jsonPath("$.result.content.[0].restName").value(restName),
                        jsonPath("$.result.content.[0].latitude").value(latitude),
                        jsonPath("$.result.content.[0].longitude").value(longitude)
                )
                .andDo(restDocs.document(

                        requestParameters(
                                parameterWithName("groupId").description("그룹 아이디")
                        ),
                        responseFields(
                                fieldWithPath("success").description("성공 여부"),
                                fieldWithPath("result.content.[].restId").description("식당 아이디"),
                                fieldWithPath("result.content.[].restName").description("식당 이름"),
                                fieldWithPath("result.content.[].latitude").description("위도"),
                                fieldWithPath("result.content.[].longitude").description("경도"),

                                fieldWithPath("result.totalElements").description("전체 데이터 수"),
                                fieldWithPath("result.totalPages").description("전체 페이지 수"),
                                fieldWithPath("result.first").description("첫 페이지 여부"),
                                fieldWithPath("result.last").description("마지막 페이지 여부"),
                                fieldWithPath("result.empty").description("빈 데이터 여부"),
                                fieldWithPath("result.size").description("사이즈"),
                                fieldWithPath("result.number").description("페이지"),

                                fieldWithPath("result.numberOfElements").ignored(),
                                fieldWithPath("result.pageable.*").ignored(),
                                fieldWithPath("result.pageable.sort.*").ignored(),
                                fieldWithPath("result.sort.*").ignored()
                        )));
    }
}
