package jparest.practice.rest.service;

import jparest.practice.group.domain.Group;
import jparest.practice.group.dto.CreateGroupRequest;
import jparest.practice.group.dto.CreateGroupResponse;
import jparest.practice.group.exception.GroupNotFoundException;
import jparest.practice.group.repository.GroupRepository;
import jparest.practice.group.service.GroupService;
import jparest.practice.rest.dto.AddFavoriteRestRequest;
import jparest.practice.rest.dto.GetMostSavedRestResponse;
import jparest.practice.rest.dto.GetNewSavedRestResponse;
import jparest.practice.user.domain.User;
import jparest.practice.user.service.UserAuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static jparest.practice.common.fixture.GroupFixture.groupNameList;
import static jparest.practice.common.fixture.RestFixture.*;
import static jparest.practice.common.fixture.UserFixture.createFirstUser;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
class RankingRestaurantServiceTest {

    private User firstUser;
    private List<Group> groupList = new ArrayList<>();

    @Autowired
    UserAuthService userAuthService;

    @Autowired
    GroupService groupService;

    @Autowired
    GroupRepository groupRepository;

    @Autowired
    FavoriteRestaurantService favoriteRestaurantService;

    @Autowired
    RankingRestaurantService rankingRestaurantService;

    @BeforeEach
    void setUp() {
        firstUser = userAuthService.join(createFirstUser());
    }

    @Test
    public void 가장_많이_저장된_맛집_순위_리스트_조회() throws Exception {

        //given

        // 5 개의 그룹 생성
        createGroupList(5);

        // 각 그룹 별로 index 개의 맛집을 추가한다. (ex. groupList.get(2) -> 2개 맛집 추가)
        for (int i = 0; i < groupList.size(); i++) {

            for (int j = 0; j < i; j++) {
                AddFavoriteRestRequest addFavRestRequest = createAddFavoriteRestRequest(
                        groupList.get(i).getId(), restNameList.get(j)
                );

                favoriteRestaurantService.addFavRest(firstUser, restIdList.get(j), addFavRestRequest);
            }
        }

        //when
        List<GetMostSavedRestResponse> result = rankingRestaurantService.getMostSavedRest();

        List<GetMostSavedRestResponse> sortedResult = result.stream()
                .sorted(Comparator.comparing(GetMostSavedRestResponse::getTotalFavorite)
                        .reversed())
                .toList();

        //then
        assertAll(
                () -> assertEquals(result.get(0).getRank(), 1),
                () -> {
                    assertEquals(result, sortedResult, "결과 값은 맛집 저장 횟수를 기준으로 내림차순 정렬되어 있어야 한다.");
                }
        );
    }

    @Test
    public void 가장_최근에_저장된_맛집_순위_리스트_조회() throws Exception {

        //given

        // 1 개의 그룹 생성
        createGroupList(5);
        
        // index 순서대로 맛집을 저장한다.
        for (int i = 0; i < restIdList.size(); i++) {
            AddFavoriteRestRequest addFavRestRequest = createAddFavoriteRestRequest(
                    groupList.get(0).getId(), restNameList.get(i)
            );

            favoriteRestaurantService.addFavRest(firstUser, restIdList.get(i), addFavRestRequest);
        }

        //when
        List<GetNewSavedRestResponse> result = rankingRestaurantService.getNewSavedRest();
        
        List<GetNewSavedRestResponse> sortedResult = result.stream()
                .sorted(Comparator.comparing(GetNewSavedRestResponse::getSavedAt)
                        .reversed())
                .toList();

        //then
        assertAll(
                () -> assertEquals(result.get(0).getRank(), 1),
                () -> {
                    assertEquals(result, sortedResult, "결과 값은 저장된 시간을 기준으로 내림차순 정렬되어 있어야 한다.");
                }
        );
    }


    private Group findGroupById(Long groupId) {
       return groupRepository.findById(groupId).orElseThrow(() -> new GroupNotFoundException("groupId" + groupId));
    }

    private void createGroupList(int count) {
        for (int i = 0; i < count; i++) {
            CreateGroupRequest createGroupRequest = CreateGroupRequest.builder()
                    .groupName(groupNameList.get(i))
                    .isPublic(true)
                    .build();

            CreateGroupResponse groupResponse = groupService.createGroup(firstUser, createGroupRequest);

            Group group = findGroupById(groupResponse.getId());
            groupList.add(group);
        }
    }
}
