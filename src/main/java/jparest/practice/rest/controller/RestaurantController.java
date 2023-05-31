package jparest.practice.rest.controller;

import jparest.practice.auth.security.CurrentUser;
import jparest.practice.common.util.ApiResult;
import jparest.practice.common.util.ApiUtils;
import jparest.practice.rest.dto.AddFavoriteRestRequest;
import jparest.practice.rest.dto.GetFavRestListResponse;
import jparest.practice.rest.service.RestService;
import jparest.practice.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class RestaurantController {

    private final RestService restService;

    @PostMapping("/group/{groupId}/favorite/rest/{restId}")
    public ApiResult<Boolean> addFavRest(@CurrentUser User user,
                                              @PathVariable Long groupId,
                                              @PathVariable String restId,
                                              @RequestBody AddFavoriteRestRequest addFavoriteRestRequest
                                              ) {
        return ApiUtils.success(restService.addFavRest(user, groupId, restId, addFavoriteRestRequest.getRestName(), addFavoriteRestRequest.getLatitude(), addFavoriteRestRequest.getLongitude()));
    }

    @DeleteMapping("/group/{groupId}/favorite/rest/{restId}")
    public ApiResult<Boolean> deleteFavRest(@CurrentUser User user,
                                                 @PathVariable Long groupId,
                                                 @PathVariable String restId
    ) {
        return ApiUtils.success(restService.deleteFavRest(user, groupId, restId));
    }

    @GetMapping("/group/{groupId}/favorite/rests")
    public ApiResult<List<GetFavRestListResponse>> getFavRestList(@CurrentUser User user,
                                                                  @PathVariable Long groupId) {

        return ApiUtils.success(restService.getFavRestList(user, groupId));
    }
}
