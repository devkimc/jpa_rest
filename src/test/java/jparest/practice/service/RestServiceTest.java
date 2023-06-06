package jparest.practice.service;


import jparest.practice.group.domain.Group;
import jparest.practice.group.dto.CreateGroupResponse;
import jparest.practice.group.exception.GroupNotFoundException;
import jparest.practice.group.repository.GroupRepository;
import jparest.practice.group.service.GroupService;
import jparest.practice.invite.domain.InviteStatus;
import jparest.practice.invite.dto.InviteUserRequest;
import jparest.practice.invite.dto.InviteUserResponse;
import jparest.practice.invite.service.InviteService;
import jparest.practice.rest.domain.GroupRest;
import jparest.practice.rest.domain.Rest;
import jparest.practice.rest.dto.GetFavRestListResponse;
import jparest.practice.rest.exception.ExistGroupRestException;
import jparest.practice.rest.exception.GroupRestNotFoundException;
import jparest.practice.rest.exception.RestNotFoundException;
import jparest.practice.rest.repository.GroupRestRepository;
import jparest.practice.rest.repository.RestRepository;
import jparest.practice.rest.service.RestService;
import jparest.practice.user.domain.User;
import jparest.practice.user.service.UserAuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static jparest.practice.common.utils.fixture.GroupFixture.*;
import static jparest.practice.common.utils.fixture.RestFixture.*;
import static jparest.practice.common.utils.fixture.UserFixture.*;
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
    InviteService inviteService;

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

        // 3. 그룹 초대, 초대 승낙
        InviteUserRequest inviteUserRequest = new InviteUserRequest(secondUser.getId(), group.getId());
        InviteUserResponse inviteResponse = inviteService.inviteToGroup(firstUser, inviteUserRequest);

        inviteService.procInvitation(inviteResponse.getInviteId(), secondUser, InviteStatus.ACCEPT);
    }

    @Test
    public void 식당테이블에_존재하지_않는_맛집_추가() throws Exception {

        //given

        //when
        restService.addFavRest(firstUser, restId, createFavoriteRest(group.getId()));

        GroupRest groupRest = findByGroupIdAndRestId(group.getId(), restId);

        //then
        assertAll(
                () -> assertEquals(restName, groupRest.getRest().getRestname()),
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

        //when
        List<GetFavRestListResponse> favRestList = restService.getFavRestList(firstUser, group.getId());

        //then
        assertAll(
                () -> assertEquals(1, favRestList.size()),
                () -> assertEquals(latitude, favRestList.get(0).getLatitude()),
                () -> assertEquals(longitude, favRestList.get(0).getLongitude())
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
