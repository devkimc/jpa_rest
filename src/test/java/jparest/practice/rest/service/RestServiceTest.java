package jparest.practice.rest.service;


import jparest.practice.group.domain.Group;
import jparest.practice.group.dto.CreateGroupResponse;
import jparest.practice.group.exception.GroupNotFoundException;
import jparest.practice.group.repository.GroupRepository;
import jparest.practice.group.service.GroupService;
import jparest.practice.invite.service.InviteService;
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

import static jparest.practice.common.utils.fixture.GroupFixture.groupName1;
import static jparest.practice.common.utils.fixture.RestFixture.*;
import static jparest.practice.common.utils.fixture.UserFixture.createFirstUser;
import static jparest.practice.common.utils.fixture.UserFixture.createSecondUser;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class RestServiceTest {

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
    RestService restService;

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
        CreateGroupResponse groupResponse = groupService.createGroup(firstUser, groupName1);
        group = findGroupById(groupResponse.getId());
    }

    @Test
    public void 식당테이블에_존재하지_않는_맛집_추가() throws Exception {

        //given

        //when
        restService.addFavRest(firstUser, restId, createFavoriteRest(group.getId()));

        GroupRest groupRest = findByGroupIdAndRestId(group.getId(), restId);

        //then
        assertAll(
                () -> assertEquals(restName, groupRest.getRest().getRestName()),
                () -> assertEquals(restId, findRestById(restId).getId())
        );
    }

    @Test
    public void 맛집테이블에_존재하는_맛집_추가시_에러() throws Exception {

        //given
        restService.addFavRest(firstUser, restId, createFavoriteRest(group.getId()));

        //when
        //then
        assertThrows(
                ExistGroupRestException.class,
                () -> restService.addFavRest(firstUser, restId, createFavoriteRest(group.getId()))
        );

    }

    @Test
    public void 맛집테이블에_존재하는_맛집_삭제후_조회시_에러() throws Exception {
        
        //given
        restService.addFavRest(firstUser, restId, createFavoriteRest(group.getId()));

        GroupRest groupRest = findByGroupIdAndRestId(group.getId(), restId);

        //when
        restService.deleteFavRest(firstUser, group.getId(), restId);

        //then
        assertThrows(
                GroupRestNotFoundException.class, () -> findGroupRestById(groupRest.getId())
        );
    }

    @Test
    public void 맛집테이블에_존재하지_않는_맛집_삭제시_에러() throws Exception {

        //given
        //when
        //맛집이 하나도 추가되지 않은 상태

        //then
        assertThrows(
                GroupRestNotFoundException.class, () -> restService.deleteFavRest(firstUser, group.getId(), restId)
        );
    }

    @Test
    public void 그룹맛집_리스트_조회() throws Exception {

        //given
        restService.addFavRest(firstUser, restId, createFavoriteRest(group.getId()));
        PageRequest pageRequest = PageRequest.of(0, 10);

        //when
        Page<GetFavRestListResponse> favRestList = restService.getFavRestList(group.getId(), pageRequest);

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
