package jparest.practice.rest.controller;

import jparest.practice.auth.security.CurrentUser;
import jparest.practice.common.util.ApiResult;
import jparest.practice.common.util.ApiUtils;
import jparest.practice.rest.dto.AddFavoriteRestRequest;
import jparest.practice.rest.dto.GetFavRestListResponse;
import jparest.practice.rest.service.RestService;
import jparest.practice.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/restaurants")
public class RestaurantController {

    private final RestService restService;

    @PostMapping("/{restId}/favorite")
    public ApiResult<Boolean> addFavRest(@CurrentUser User user,
                                              @PathVariable String restId,
                                              @RequestBody AddFavoriteRestRequest addFavoriteRestRequest
                                              ) {
        return ApiUtils.success(restService.addFavRest(user, restId, addFavoriteRestRequest));
    }

    @DeleteMapping("/{restId}/favorite")
    public ApiResult<Boolean> deleteFavRest(@CurrentUser User user,
                                            @PathVariable String restId,
                                            @RequestParam Long groupId
    ) {
        return ApiUtils.success(restService.deleteFavRest(user, groupId, restId));
    }

    @GetMapping("/favorite")
    public ApiResult<Page<GetFavRestListResponse>> getFavRestList(@CurrentUser User user,
                                                                  @RequestParam Long groupId,
                                                                  Pageable pageable
    ) {
        return ApiUtils.success(restService.getFavRestList(groupId, pageable));
    }
}
