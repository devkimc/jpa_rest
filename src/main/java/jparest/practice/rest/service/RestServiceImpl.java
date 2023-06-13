package jparest.practice.rest.service;

import jparest.practice.group.domain.Group;
import jparest.practice.group.exception.GroupNotFoundException;
import jparest.practice.group.repository.GroupRepository;
import jparest.practice.rest.domain.GroupRest;
import jparest.practice.rest.domain.Rest;
import jparest.practice.rest.dto.AddFavoriteRestRequest;
import jparest.practice.rest.dto.GetFavRestListResponse;
import jparest.practice.rest.exception.ExistGroupRestException;
import jparest.practice.rest.exception.GroupRestNotFoundException;
import jparest.practice.rest.repository.GroupRestRepository;
import jparest.practice.rest.repository.RestRepository;
import jparest.practice.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RestServiceImpl implements RestService {

    private final GroupRepository groupRepository;
    private final RestRepository restRepository;
    private final GroupRestRepository groupRestRepository;

    @Override
    @Transactional
    public Boolean addFavRest(User user, String restId, AddFavoriteRestRequest addFavoriteRestRequest) {

        Long groupId = addFavoriteRestRequest.getGroupId();

        // 1. 그룹에 가입됐는지 확인
        if (!user.isJoinGroup(groupId)) {
            throw new GroupNotFoundException("groupId = " + groupId);
        }

        Optional<GroupRest> existGroupRest = groupRestRepository.findByGroupIdAndRestId(groupId, restId);

        // 2. 추가하려는 맛집이 그룹 맛집에 존재하면 에러
        if (existGroupRest.isPresent()) {
            throw new ExistGroupRestException("이미 존재하는 맛집 = " + existGroupRest.get().getId());
        }

        Optional<Rest> optionalFindRest = restRepository.findById(restId);

        // 3. 식당 테이블에 존재하지 않는 맛집이면 식당, 맛집 저장
        if (optionalFindRest.isEmpty()) {
            Rest rest = Rest.builder().id(restId)
                    .restName(addFavoriteRestRequest.getRestName())
                    .latitude(addFavoriteRestRequest.getLatitude())
                    .longitude(addFavoriteRestRequest.getLongitude())
                    .totalFavorite(1)
                    .build();

            Rest saveRest = restRepository.save(rest);

            saveGroupRest(groupId, saveRest);
            return true;
        }

        // 4. REST DB 에 존재하면, 맛집 저장
        Rest findRest = optionalFindRest.get();

        findRest.increaseTotalFavorite();

        saveGroupRest(groupId, findRest);
        restRepository.save(findRest);
        return true;
    }

    @Override
    @Transactional
    public Boolean deleteFavRest(User user, Long groupId, String restId) {

        if (!user.isJoinGroup(groupId)) {
            throw new GroupNotFoundException("groupId = " + groupId);
        }

        GroupRest existGroupRest = findByGroupIdAndRestId(groupId, restId);

        existGroupRest.getRest().decreaseTotalFavorite();

        groupRestRepository.delete(existGroupRest);

        return true;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<GetFavRestListResponse> getFavRestList(Long groupId, Pageable pageable) {
        Page<GetFavRestListResponse> getFavRestListResponses = restRepository.findAllByGroupId(groupId, pageable);
        return getFavRestListResponses;
    }

    private void saveGroupRest(Long groupId, Rest rest) {
//        GroupRest groupRest =  groupRest.createGroupRest(findGroupById(groupId), rest);
        GroupRest groupRest = GroupRest.createGroupRest(findGroupById(groupId), rest);

        groupRestRepository.save(groupRest);
    }

    private Group findGroupById(Long groupId) {
        return groupRepository.findById(groupId).orElseThrow(() -> new GroupNotFoundException("groupId = " + groupId));
    }

    private GroupRest findByGroupIdAndRestId(Long groupId, String restId) {
        return groupRestRepository.findByGroupIdAndRestId(groupId, restId).orElseThrow(() -> new GroupRestNotFoundException("groupId = " + groupId + " restId = " + restId));
    }
}
