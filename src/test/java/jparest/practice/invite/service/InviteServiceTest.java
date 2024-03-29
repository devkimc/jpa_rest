package jparest.practice.invite.service;

import jparest.practice.group.domain.Group;
import jparest.practice.group.domain.GroupUser;
import jparest.practice.group.domain.GroupUserType;
import jparest.practice.group.dto.CreateGroupRequest;
import jparest.practice.group.dto.CreateGroupResponse;
import jparest.practice.group.exception.ExistGroupUserException;
import jparest.practice.group.exception.GroupNotFoundException;
import jparest.practice.group.repository.GroupRepository;
import jparest.practice.group.service.GroupService;
import jparest.practice.invite.domain.Invite;
import jparest.practice.invite.domain.InviteStatus;
import jparest.practice.invite.dto.GetWaitingInviteResponse;
import jparest.practice.invite.dto.InviteUserRequest;
import jparest.practice.invite.dto.InviteUserResponse;
import jparest.practice.invite.dto.ProcessInviteRequest;
import jparest.practice.invite.exception.ExistWaitingInviteException;
import jparest.practice.invite.exception.InviteNotFoundException;
import jparest.practice.invite.repository.InviteRepository;
import jparest.practice.user.domain.User;
import jparest.practice.user.service.UserAuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static jparest.practice.common.fixture.GroupFixture.groupName1;
import static jparest.practice.common.fixture.UserFixture.*;
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

        CreateGroupRequest createGroupRequest = CreateGroupRequest.builder()
                .groupName(groupName1)
                .isPublic(true)
                .build();

        CreateGroupResponse response = groupService.createGroup(firstUser, createGroupRequest);
        group = findGroupById(response.getId());
    }

    @Test
    public void 그룹초대() throws Exception {

        //given
        List<GroupUser> groupUsers = firstUser.getGroupUsers();
        InviteUserRequest inviteUserRequest = new InviteUserRequest(secondUser.getId(), group.getId());

        //when
        InviteUserResponse response = inviteService.inviteToGroup(firstUser, inviteUserRequest);
        Invite invite = findInviteById(response.getInviteId());

        //then
        assertAll(
                () -> assertEquals(InviteStatus.WAITING, invite.getStatus()), // 초대 상태
                () -> assertEquals(groupUsers.get(0), invite.getSendGroupUser()), // 초대한 유저
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
        inviteService.processInvite(secondUser, invite.getId(), new ProcessInviteRequest(InviteStatus.ACCEPT));

        GroupUser groupUser = group.getGroupUsers().get(1);

        //then
        assertAll(
                () -> assertEquals(InviteStatus.ACCEPT, invite.getStatus(), "그룹초대 승낙 시 초대상태는 ACCEPT 이다."),
                () -> assertEquals(groupUser.getUser(), secondUser),
                () -> assertEquals(groupUser.getGroupUserType(), GroupUserType.ROLE_MEMBER,
                        "초대를 통해 그룹에 들어온 유저의 역할은 MEMBER 이다.")
        );
    }

    @Test
    public void 그룹초대_거절() throws Exception {
        //given
        InviteUserRequest inviteUserRequest = new InviteUserRequest(secondUser.getId(), group.getId());
        InviteUserResponse response = inviteService.inviteToGroup(firstUser, inviteUserRequest);

        Invite invite = findInviteById(response.getInviteId());

        //when
        inviteService.processInvite(secondUser, invite.getId(), new ProcessInviteRequest(InviteStatus.REJECT));

        //then
        assertEquals(InviteStatus.REJECT, invite.getStatus());
    }

    @Test
    public void 그룹초대_취소() throws Exception {
        //given
        InviteUserRequest inviteUserRequest = new InviteUserRequest(secondUser.getId(), group.getId());
        InviteUserResponse response = inviteService.inviteToGroup(firstUser, inviteUserRequest);

        Invite invite = findInviteById(response.getInviteId());

        //when
        inviteService.processInvite(firstUser, invite.getId(), new ProcessInviteRequest(InviteStatus.CANCEL));

        //then
        assertEquals(InviteStatus.CANCEL, invite.getStatus());
    }

    @Test
    public void 그룹에_존재하는_유저를_초대시_에러() throws Exception {

        //given
        InviteUserRequest inviteUserRequest = new InviteUserRequest(secondUser.getId(), group.getId());
        InviteUserResponse response = inviteService.inviteToGroup(firstUser, inviteUserRequest);

        Invite invite = findInviteById(response.getInviteId());

        //when
        inviteService.processInvite(secondUser, invite.getId(), new ProcessInviteRequest(InviteStatus.ACCEPT));

        //then
        assertThrows(ExistGroupUserException.class,
                () -> inviteService.inviteToGroup(firstUser, inviteUserRequest));
    }

    @Test
    public void 그룹에_대기중인_초대가_존재하는_유저를_초대시_에러() throws Exception {

        //given
        InviteUserRequest inviteUserRequest = new InviteUserRequest(secondUser.getId(), group.getId());

        //when
        inviteService.inviteToGroup(firstUser, inviteUserRequest);

        //then
        assertThrows(ExistWaitingInviteException.class,
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
