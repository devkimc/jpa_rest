package jparest.practice.rest.service;

import jparest.practice.rest.dto.GetFavRestListResponse;
import jparest.practice.user.domain.User;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public class RestServiceImpl implements RestService {

    @Override
    @Transactional
    public Boolean addFavRest(User user, Long groupId, String restId, String restName, double latCdnt, double lngCdnt) {

    }

    @Override
    public Boolean deleteFavRest(User user, Long groupId, String restId) {
        return null;
    }

    @Override
    public List<GetFavRestListResponse> getFavRestList(User user, Long groupId) {
        return null;
    }
}
