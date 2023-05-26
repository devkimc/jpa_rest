package jparest.practice.rest.service;

import jparest.practice.rest.dto.GetFavRestListResponse;
import jparest.practice.user.domain.User;

import java.util.List;

public interface RestService {
    Boolean addFavRest(User user, Long groupId, String restId, String restName, double latCdnt, double lngCdnt);

    Boolean deleteFavRest(User user, Long groupId, String restId);

    List<GetFavRestListResponse> getFavRestList(User user, Long groupId);
}
