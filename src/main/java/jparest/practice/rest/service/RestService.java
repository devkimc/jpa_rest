package jparest.practice.rest.service;

import jparest.practice.rest.dto.AddFavoriteRestRequest;
import jparest.practice.rest.dto.GetFavRestListResponse;
import jparest.practice.user.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RestService {
    Boolean addFavRest(User user, String restId, AddFavoriteRestRequest addFavoriteRestRequest);

    Boolean deleteFavRest(User user, Long groupId, String restId);

    Page<GetFavRestListResponse> getFavRestList(Long groupId, Pageable pageable);
}
