package jparest.practice.rest.service;

import jparest.practice.rest.dto.AddFavoriteRestRequest;
import jparest.practice.rest.dto.GetFavRestListResponse;
import jparest.practice.user.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FavoriteRestaurantService {

    // 그룹에 맛집 추가
    Boolean addFavRest(User user, String restId, AddFavoriteRestRequest addFavoriteRestRequest);

    // 그룹에 추가된 맛집 삭제
    Boolean deleteFavRest(User user, Long groupId, String restId);

    // 그룹에 추가된 맛집 리스트 조회
    Page<GetFavRestListResponse> getFavRestList(Long groupId, Pageable pageable);
}
