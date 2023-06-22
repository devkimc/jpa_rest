package jparest.practice.rest.service;


import jparest.practice.group.domain.Group;
import jparest.practice.group.dto.CreateGroupRequest;
import jparest.practice.group.dto.CreateGroupResponse;
import jparest.practice.group.exception.GroupNotFoundException;
import jparest.practice.group.repository.GroupRepository;
import jparest.practice.group.service.GroupService;
import jparest.practice.rest.domain.GroupRest;
import jparest.practice.rest.domain.Rest;
import jparest.practice.rest.dto.GetFavRestListResponse;
import jparest.practice.rest.exception.ExistGroupRestException;
import jparest.practice.rest.exception.GroupRestNotFoundException;
import jparest.practice.rest.exception.RestNotFoundException;
import jparest.practice.rest.repository.GroupRestRepository;
import jparest.practice.rest.repository.RestRepository;
import jparest.practice.user.domain.User;
import jparest.practice.user.service.UserAuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import static jparest.practice.common.utils.fixture.GroupFixture.*;
import static jparest.practice.common.utils.fixture.RestFixture.*;
import static jparest.practice.common.utils.fixture.UserFixture.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class FavoriteRestaurantServiceTest {

    private User firstUser;
    private User secondUser;
    private Group group;

    @Autowired
    UserAuthService userAuthService;

    @Autowired
    GroupService groupService;

    @Autowired
    GroupRepository groupRepository;

    @Autowired
    FavoriteRestaurantService favoriteRestaurantService;

    @Autowired
    GroupRestRepository groupRestRepository;

    @Autowired
    RestRepository restRepository;

    @BeforeEach
    void setUp() {
        // 1. 2명의 유저 회원가입
        firstUser = userAuthService.join(createFirstUser());
        secondUser = userAuthService.join(createSecondUser());

        // 2. 그룹 생성
        CreateGroupRequest createGroupRequest = CreateGroupRequest.builder()
                .groupName(groupName1)
                .isPublic(true)
                .build();

        CreateGroupResponse groupResponse = groupService.createGroup(firstUser, createGroupRequest);
        group = findGroupById(groupResponse.getId());
    }

    @Test
    public void 식당테이블에_존재하지_않는_맛집_추가() throws Exception {

        //given

        //when
        favoriteRestaurantService.addFavRest(firstUser, restId1, createAddFavoriteRestRequest(group.getId(), restName1));

        GroupRest groupRest = findByGroupIdAndRestId(group.getId(), restId1);

        //then
        assertAll(
                () -> assertEquals(restName1, groupRest.getRest().getRestName()),
                () -> assertEquals(restId1, findRestById(restId1).getId()),
                () -> assertEquals(groupRest.getRest().getTotalFavorite(), 1, "처음 추가된 맛집은 총 북마크 맛집 개수가 1 개")
        );

    }

    @Test
    public void 맛집_추가_시_총_북마크_맛집_개수_확인() throws Exception {

        //given
        CreateGroupRequest createGroupRequest2 = CreateGroupRequest.builder()
                .groupName(groupName2)
                .isPublic(true)
                .build();

        CreateGroupResponse secondGroupResponse = groupService.createGroup(firstUser, createGroupRequest2);

        //when
        favoriteRestaurantService.addFavRest(firstUser, restId1, createAddFavoriteRestRequest(group.getId(), restName1));
        favoriteRestaurantService.addFavRest(firstUser, restId1, createAddFavoriteRestRequest(secondGroupResponse.getId(), restName1));

        GroupRest groupRest = findByGroupIdAndRestId(group.getId(), restId1);

        //then
        assertAll(
                () -> assertEquals(groupRest.getRest().getTotalFavorite(), 2, "맛집은 추가한 그룹 수 = 총 북마크 맛집 개수")
        );
    }

    @Test
    public void 그룹맛집_테이블에_존재하는_맛집_추가시_에러() throws Exception {

        //given
        favoriteRestaurantService.addFavRest(firstUser, restId1, createAddFavoriteRestRequest(group.getId(), restName1));

        //when
        //then
        assertThrows(
                ExistGroupRestException.class,
                () -> favoriteRestaurantService.addFavRest(firstUser, restId1, createAddFavoriteRestRequest(group.getId(), restName1))
        );
    }

    @Test
    public void 맛집테이블에_존재하는_맛집_삭제후_조회시_에러() throws Exception {
        
        //given
        favoriteRestaurantService.addFavRest(firstUser, restId1, createAddFavoriteRestRequest(group.getId(), restName1));

        GroupRest groupRest = findByGroupIdAndRestId(group.getId(), restId1);

        //when
        favoriteRestaurantService.deleteFavRest(firstUser, group.getId(), restId1);

        //then
        assertAll(
                () -> assertThrows(GroupRestNotFoundException.class, () -> findGroupRestById(groupRest.getId())),
                () -> assertEquals(0, groupRest.getRest().getTotalFavorite())
        );
    }

    @Test
    public void 맛집테이블에_존재하지_않는_맛집_삭제시_에러() throws Exception {

        //given
        //when
        //맛집이 하나도 추가되지 않은 상태

        //then
        assertThrows(
                GroupRestNotFoundException.class, () -> favoriteRestaurantService.deleteFavRest(firstUser, group.getId(), restId1)
        );
    }

    @Test
    public void 그룹맛집_리스트_조회() throws Exception {

        //given
        favoriteRestaurantService.addFavRest(firstUser, restId1, createAddFavoriteRestRequest(group.getId(), restName1));
        PageRequest pageRequest = PageRequest.of(0, 10);

        //when
        Page<GetFavRestListResponse> favRestList = favoriteRestaurantService.getFavRestList(group.getId(), pageRequest);

        //then
        assertAll(
                () -> assertEquals(1, favRestList.getTotalElements()),
                () -> assertEquals(latitude, favRestList.getContent().get(0).getLatitude()),
                () -> assertEquals(longitude, favRestList.getContent().get(0).getLongitude())
        );
    }

    private Group findGroupById(Long groupId) {
        return groupRepository.findById(groupId).orElseThrow(() -> new GroupNotFoundException("groupId = " + groupId));
    }

    private GroupRest findGroupRestById(Long groupRestId) {
        return groupRestRepository.findById(groupRestId).orElseThrow(() -> new GroupRestNotFoundException("groupRestId = " + groupRestId));
    }

    private GroupRest findByGroupIdAndRestId(Long groupId, String restId) {
        return groupRestRepository.findByGroupIdAndRestId(groupId, restId).orElseThrow(() -> new GroupRestNotFoundException("groupId = " + groupId + " restId = " + restId));
    }

    private Rest findRestById(String restId) {
        return restRepository.findById(restId).orElseThrow(() -> new RestNotFoundException("restId = " + restId));
    }
}
