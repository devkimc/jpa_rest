package jparest.practice.rest.service;

import jparest.practice.rest.dto.GetMostSavedRestResponse;
import jparest.practice.rest.dto.GetNewSavedRestResponse;

import java.util.List;

public interface RankingRestaurantService {

    // 가장 많이 저장된 맛집 조회
    List<GetMostSavedRestResponse> getMostSavedRest();

    // 가장 최근에 저장된 맛집 조회
    List<GetNewSavedRestResponse> getNewSavedRest();
}
