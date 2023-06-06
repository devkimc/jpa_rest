package jparest.practice.service;

import jparest.practice.group.domain.Group;
import jparest.practice.group.domain.UserGroup;
import jparest.practice.group.dto.CreateGroupResponse;
import jparest.practice.group.exception.ExistUserGroupException;
import jparest.practice.group.exception.GroupNotFoundException;
import jparest.practice.group.repository.GroupRepository;
import jparest.practice.group.service.GroupService;
import jparest.practice.invite.domain.Invite;
import jparest.practice.invite.domain.InviteStatus;
import jparest.practice.invite.dto.GetWaitingInviteResponse;
import jparest.practice.invite.dto.InviteUserRequest;
import jparest.practice.invite.dto.InviteUserResponse;
import jparest.practice.invite.exception.ExistInviteForUserException;
import jparest.practice.invite.exception.InviteNotFoundException;
import jparest.practice.invite.repository.InviteRepository;
import jparest.practice.invite.service.InviteService;
import jparest.practice.user.domain.User;
import jparest.practice.user.service.UserAuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static jparest.practice.common.utils.GroupFixture.*;
import static jparest.practice.common.utils.UserFixture.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class InviteServiceTest {
    
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
    InviteRepository inviteRepository;

    @BeforeEach
    void setUp() {
        firstUser = userAuthService.join(createFirstUser());
        secondUser = userAuthService.join(createSecondUser());

        CreateGroupResponse response = groupService.createGroup(firstUser, groupName1);
        group = findGroupById(response.getId());
    }

    @Test
    public void 그룹초대() throws Exception {

        //given
        List<UserGroup> userGroups = firstUser.getUserGroups();
        InviteUserRequest inviteUserRequest = new InviteUserRequest(secondUser.getId(), group.getId());

        //when
        InviteUserResponse response = inviteService.inviteToGroup(firstUser, inviteUserRequest);
        Invite invite = findInviteById(response.getInviteId());

        //then
        assertAll(
                () -> assertEquals(InviteStatus.WAITING, invite.getInviteStatus()), // 초대 상태
                () -> assertEquals(userGroups.get(0), invite.getSendUserGroup()), // 초대한 유저
                () -> assertEquals(secondUser, invite.getRecvUser()) // 초대받은 유저
        );
    }

    @Test
    public void 그룹초대_승낙() throws Exception {

        //given
        InviteUserRequest inviteUserRequest = new InviteUserRequest(secondUser.getId(), group.getId());
        InviteUserResponse response = inviteService.inviteToGroup(firstUser, inviteUserRequest);

        Invite invite = findInviteById(response.getInviteId());

        //when
        inviteService.procInvitation(invite.getId(), secondUser, InviteStatus.ACCEPT);

        //then
        assertAll(
                ()-> assertEquals(InviteStatus.ACCEPT, invite.getInviteStatus()), // 초대 승낙
                ()-> assertEquals(group.getUserGroups().get(1).getUser(), secondUser) // 그룹원 추가
        );
    }

    @Test
    public void 그룹초대_거절() throws Exception {
        //given
        InviteUserRequest inviteUserRequest = new InviteUserRequest(secondUser.getId(), group.getId());
        InviteUserResponse response = inviteService.inviteToGroup(firstUser, inviteUserRequest);

        Invite invite = findInviteById(response.getInviteId());

        //when
        inviteService.procInvitation(invite.getId(), secondUser, InviteStatus.REJECT);

        //then
        assertEquals(InviteStatus.REJECT, invite.getInviteStatus());
    }

    @Test
    public void 그룹초대_취소() throws Exception {
        //given
        InviteUserRequest inviteUserRequest = new InviteUserRequest(secondUser.getId(), group.getId());
        InviteUserResponse response = inviteService.inviteToGroup(firstUser, inviteUserRequest);

        Invite invite = findInviteById(response.getInviteId());

        //when
        inviteService.procInvitation(invite.getId(), firstUser, InviteStatus.CANCEL);

        //then
        assertEquals(InviteStatus.CANCEL, invite.getInviteStatus());
    }

    @Test
    public void 그룹에_존재하는_유저를_초대시_에러() throws Exception {

        //given
        InviteUserRequest inviteUserRequest = new InviteUserRequest(secondUser.getId(), group.getId());
        InviteUserResponse response = inviteService.inviteToGroup(firstUser, inviteUserRequest);

        Invite invite = findInviteById(response.getInviteId());

        //when
        inviteService.procInvitation(invite.getId(), secondUser, InviteStatus.ACCEPT);

        //then
        assertThrows(ExistUserGroupException.class,
                () -> inviteService.inviteToGroup(firstUser, inviteUserRequest));
    }

    @Test
    public void 그룹에_대기중인_초대가_존재하는_유저를_초대시_에러() throws Exception {

        //given
        InviteUserRequest inviteUserRequest = new InviteUserRequest(secondUser.getId(), group.getId());

        //when
        inviteService.inviteToGroup(firstUser, inviteUserRequest);

        //then
        assertThrows(ExistInviteForUserException.class,
                () -> inviteService.inviteToGroup(firstUser, inviteUserRequest));
    }

    @Test
    public void 대기중인_초대리스트_조회() throws Exception {

        //given
        InviteUserRequest inviteUserRequest = new InviteUserRequest(secondUser.getId(), group.getId());
        InviteUserResponse response = inviteService.inviteToGroup(firstUser, inviteUserRequest);

        Invite invite = findInviteById(response.getInviteId());

        //when
        List<GetWaitingInviteResponse> inviteList = inviteService.getWaitingInviteList(secondUser);

        //then
        assertAll(
                () -> assertEquals(invite.getId(), inviteList.get(0).getInviteId()),
                () -> assertEquals(firstUser.getNickname(), inviteList.get(0).getNickName()),
                () -> assertEquals(groupName1, inviteList.get(0).getGroupName())
        );
    }

    @Test
    public void 초대리스트가_존재하지_않을_경우_조회하면_길이0() throws Exception {

        //given

        //when
        List<GetWaitingInviteResponse> inviteList = inviteService.getWaitingInviteList(secondUser);

        //then
        assertEquals(inviteList.size(), 0);
    }

    private Group findGroupById(Long groupId) {
        return groupRepository.findById(groupId).orElseThrow(() -> new GroupNotFoundException("groupId = " + groupId));
    }

    private Invite findInviteById(Long inviteId) {
        return inviteRepository.findById(inviteId).orElseThrow(() -> new InviteNotFoundException("inviteId = " + inviteId));
    }
}
