package jparest.practice.rest.service;

import jparest.practice.group.domain.Group;
import jparest.practice.group.exception.GroupNotFoundException;
import jparest.practice.group.repository.GroupRepository;
import jparest.practice.rest.domain.GroupRest;
import jparest.practice.rest.domain.Rest;
import jparest.practice.rest.dto.GetFavRestListResponse;
import jparest.practice.rest.exception.ExistGroupRestException;
import jparest.practice.rest.exception.GroupRestNotFoundException;
import jparest.practice.rest.exception.RestNotFoundException;
import jparest.practice.rest.repository.GroupRestRepository;
import jparest.practice.rest.repository.RestRepository;
import jparest.practice.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RestServiceImpl implements RestService {

    private final GroupRepository groupRepository;
    private final RestRepository restRepository;
    private final GroupRestRepository groupRestRepository;

    @Override
    @Transactional
    public Boolean addFavRest(User user, Long groupId, String restId, String restName, double latitude, double longitude) {
        Optional<GroupRest> existGroupRest = groupRestRepository.findByGroupIdAndRestId(groupId, restId);

        // 1. 추가하려는 맛집이 그룹 맛집에 존재하면 에러
        if (existGroupRest.isPresent()) {
            throw new ExistGroupRestException("이미 존재하는 맛집 = " + existGroupRest.get().getId());
        }

        Optional<Rest> findRest = restRepository.findById(restId);

        // 2. REST DB 에 존재하지 않는 맛집이면 저장
        if (findRest.isEmpty()) {
            Rest rest = Rest.builder().id(restId)
                    .restname(restName)
                    .latitude(latitude)
                    .longitude(longitude)
                    .build();

            restRepository.save(rest);
        }

        // 3. GROUP_REST DB 에 저장
        GroupRest groupRest = new GroupRest(findGroupById(groupId), findRest.get());
        groupRestRepository.save(groupRest);

        return true;
    }

    @Override
    public Boolean deleteFavRest(User user, Long groupId, String restId) {
        return null;
    }

    @Override
    public List<GetFavRestListResponse> getFavRestList(User user, Long groupId) {
        return null;
    }

    private Group findGroupById(Long groupId) {
        return groupRepository.findById(groupId).orElseThrow(() -> new GroupNotFoundException("groupId = " + groupId));
    }

    private Rest findRestById(String restId) {
        return restRepository.findById(restId).orElseThrow(() -> new RestNotFoundException("restId = " + restId));
    }

    private GroupRest findByGroupIdAndRestId(Long groupId, String restId) {
        return groupRestRepository.findByGroupIdAndRestId(groupId, restId).orElseThrow(() -> new GroupRestNotFoundException("groupId = " + groupId + " restId = " + restId));
    }

    private GroupRest findGroupRest(Long groupRestId) {
        return groupRestRepository.findById(groupRestId).orElseThrow(() -> new GroupRestNotFoundException("groupRestId = " + groupRestId));
    }
}