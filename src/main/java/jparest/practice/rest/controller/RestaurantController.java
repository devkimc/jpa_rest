package jparest.practice.rest.controller;

import jparest.practice.auth.security.CurrentUser;
import jparest.practice.common.util.ApiResult;
import jparest.practice.rest.dto.AddFavoriteRestRequest;
import jparest.practice.rest.dto.GetFavRestListResponse;
import jparest.practice.user.domain.User;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class RestaurantController {

    @PostMapping("/group/{groupId}/favorite/rest/{restId}")
    public ApiResult<Boolean> addFavRest(@CurrentUser User user,
                                              @PathVariable Long groupId,
                                              @PathVariable String restId,
                                              @RequestBody AddFavoriteRestRequest addFavoriteRestRequest
                                              ) {
        return null;
    }

    @DeleteMapping("/group/{groupId}/favorite/rest/{restId}")
    public ApiResult<Boolean> deleteFavRest(@CurrentUser User user,
                                                 @PathVariable Long groupId,
                                                 @PathVariable String restId
    ) {
        return null;
    }

    @GetMapping("/group/{groupId}/favorite/rests")
    public ApiResult<List<GetFavRestListResponse>> getFavRestList(@CurrentUser User user,
                                                                  @PathVariable Long groupId) {

        return null;
    }
}
