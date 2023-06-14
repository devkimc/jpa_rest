package jparest.practice.rest.controller;


import jparest.practice.common.util.ApiResult;
import jparest.practice.common.util.ApiUtils;
import jparest.practice.rest.dto.GetMostSavedRestResponse;
import jparest.practice.rest.dto.GetNewSavedRestResponse;
import jparest.practice.rest.service.RankingRestaurantService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/restaurant/ranking")
@RequiredArgsConstructor
public class RankingRestaurantController {

    private final RankingRestaurantService rankingRestaurantService;

    @GetMapping("/most/save")
    public ApiResult<List<GetMostSavedRestResponse>> getMostSavedRest() {
        return ApiUtils.success(rankingRestaurantService.getMostSavedRest());
    }

    @GetMapping("/new/save")
    public ApiResult<List<GetNewSavedRestResponse>> getNewSavedRest() {
        return ApiUtils.success(rankingRestaurantService.getNewSavedRest());
    }
}
