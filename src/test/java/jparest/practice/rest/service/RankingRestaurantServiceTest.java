package jparest.practice.rest.service;

import jparest.practice.group.domain.Group;
import jparest.practice.group.dto.CreateGroupResponse;
import jparest.practice.group.exception.GroupNotFoundException;
import jparest.practice.group.repository.GroupRepository;
import jparest.practice.group.service.GroupService;
import jparest.practice.rest.dto.AddFavoriteRestRequest;
import jparest.practice.rest.dto.GetMostSavedRestResponse;
import jparest.practice.user.domain.User;
import jparest.practice.user.service.UserAuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static jparest.practice.common.utils.fixture.GroupFixture.groupNameList;
import static jparest.practice.common.utils.fixture.RestFixture.*;
import static jparest.practice.common.utils.fixture.UserFixture.createFirstUser;
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

        // 5 개의 그룹 생성
        for (String groupName : groupNameList) {
            CreateGroupResponse groupResponse = groupService.createGroup(firstUser, groupName);

            Group group = findGroupById(groupResponse.getId());
            groupList.add(group);
        }
    }

    @Test
    public void 가장_많이_저장된_맛집_순위_리스트_조회() throws Exception {

        //given

        // 각 그룹 별로 index 개의 맛집을 추가한다. (ex. group2 -> 2개 맛집 추가)
        for (int i = 0; i < groupList.size(); i++) {

            for (int j = 0; j < i; j++) {
                AddFavoriteRestRequest addFavRestRequest = createAddFavoriteRestRequest(
                        groupList.get(i).getId(), restNameList.get(j)
                );

                favoriteRestaurantService.addFavRest(firstUser, restIdList.get(j), addFavRestRequest);
            }
        }

        //when
        List<GetMostSavedRestResponse> mostSavedRestList = rankingRestaurantService.getMostSavedRest();

        //then
        assertAll(
                () -> assertEquals(mostSavedRestList.get(0).getRank(), 1)
        );
    }


    private Group findGroupById(Long groupId) {
       return groupRepository.findById(groupId).orElseThrow(() -> new GroupNotFoundException("groupId" + groupId));
    }

}