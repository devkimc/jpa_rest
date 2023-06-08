package jparest.practice.rest.service;

import jparest.practice.rest.dto.AddFavoriteRestRequest;
import jparest.practice.rest.dto.GetFavRestListResponse;
import jparest.practice.user.domain.User;

import java.util.List;

public interface RestService {
    Boolean addFavRest(User user, String restId, AddFavoriteRestRequest addFavoriteRestRequest);

    Boolean deleteFavRest(User user, Long groupId, String restId);

    List<GetFavRestListResponse> getFavRestList(Long groupId);
}
