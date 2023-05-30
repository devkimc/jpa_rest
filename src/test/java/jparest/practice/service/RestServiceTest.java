package jparest.practice.service;


import jparest.practice.common.MockUserJoin;
import jparest.practice.group.domain.Group;
import jparest.practice.group.dto.CreateGroupResponse;
import jparest.practice.group.exception.GroupNotFoundException;
import jparest.practice.group.repository.GroupRepository;
import jparest.practice.group.service.GroupService;
import jparest.practice.invite.domain.Invite;
import jparest.practice.invite.domain.InviteStatus;
import jparest.practice.invite.dto.InviteUserResponse;
import jparest.practice.invite.exception.InviteNotFoundException;
import jparest.practice.invite.repository.InviteRepository;
import jparest.practice.invite.service.InviteService;
import jparest.practice.rest.domain.GroupRest;
import jparest.practice.rest.domain.Rest;
import jparest.practice.rest.exception.ExistGroupRestException;
import jparest.practice.rest.exception.GroupRestNotFoundException;
import jparest.practice.rest.exception.RestNotFoundException;
import jparest.practice.rest.repository.GroupRestRepository;
import jparest.practice.rest.repository.RestRepository;
import jparest.practice.rest.service.RestService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class RestServiceTest extends MockUserJoin {

    private final String groupName = "유저 1의 나라";
    private final String restId = "223412312";
    private final String restName = "원할머니 보쌈";
    private final double latitude = 37.481079886;
    private final double longitude = 126.9530287;

    @Autowired
    InviteService inviteService;

    @Autowired
    GroupService groupService;

    @Autowired
    GroupRepository groupRepository;

    @Autowired
    InviteRepository inviteRepository;

    @Autowired
    RestService restService;

    @Autowired
    GroupRestRepository groupRestRepository;

    @Autowired
    RestRepository restRepository;

    Group group1;

    @BeforeEach
    void setUp() {
        // 1. 2명의 유저 회원가입
        joinSetUp();

        // 2. 그룹 생성
        CreateGroupResponse groupResponse = groupService.createGroup(joinUser1, groupName);
        group1 = findGroupById(groupResponse.getId());

        // 3. 그룹 초대, 초대 승낙
        InviteUserResponse inviteResponse = inviteService.inviteToGroup(groupResponse.getId(), joinUser1, joinUser2.getId());
        inviteService.procInvitation(inviteResponse.getInviteId(), joinUser2, InviteStatus.ACCEPT);
    }

    @Test
    public void 식당테이블에_존재하지_않는_맛집_추가() throws Exception {

        //given

        //when
        restService.addFavRest(joinUser1, group1.getId(), restId, restName, latitude, longitude);

        GroupRest groupRest = findByGroupIdAndRestId(group1.getId(), restId);

        //then
        assertAll(
                () -> assertEquals(restName, groupRest.getRest().getRestname()),
                () -> assertEquals(restId, findRestById(restId).getId())
        );
    }

    @Test
    public void 맛집테이블에_존재하는_맛집_추가시_에러() throws Exception {

        //given
        restService.addFavRest(joinUser1, group1.getId(), restId, restName, latitude, longitude);

        //when
        //then
        assertThrows(
                ExistGroupRestException.class,
                () -> restService.addFavRest(joinUser1, group1.getId(), restId, restName, latitude, longitude)
        );
    }

    @Test
    public void 맛집테이블에_존재하는_맛집_삭제후_조회시_에러() throws Exception {
        
        //given
        restService.addFavRest(joinUser1, group1.getId(), restId, restName, latitude, longitude);

        GroupRest groupRest = findByGroupIdAndRestId(group1.getId(), restId);

        //when
        restService.deleteFavRest(joinUser1, group1.getId(), restId);

        //then
        assertThrows(
                GroupRestNotFoundException.class, () -> findGroupRestById(groupRest.getId())
        );
    }

    @Test
    public void 맛집테이블에_존재하지_않는_맛집_삭제시_에러() throws Exception {

        //given 맛집이 하나도 추가되지 않은 상태
        //when
        //then

        assertThrows(
                GroupRestNotFoundException.class, () -> restService.deleteFavRest(joinUser1, group1.getId(), restId)
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

    private Invite findInviteById(Long inviteId) {
        return inviteRepository.findById(inviteId).orElseThrow(() -> new InviteNotFoundException("inviteId = " + inviteId));
    }
}
