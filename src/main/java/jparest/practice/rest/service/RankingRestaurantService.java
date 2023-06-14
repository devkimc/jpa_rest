package jparest.practice.rest.service;

import jparest.practice.rest.dto.GetMostSavedRestResponse;

import java.util.List;

public interface RankingRestaurantService {
    List<GetMostSavedRestResponse> getMostSavedRest();

//    List<GetNewSavedRestResponse> getNewSavedRest();
}
