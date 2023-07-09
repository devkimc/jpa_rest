package jparest.practice.invite.service;

import jparest.practice.group.domain.Group;
import jparest.practice.group.dto.CreateGroupRequest;
import jparest.practice.group.dto.CreateGroupResponse;
import jparest.practice.group.exception.GroupNotFoundException;
import jparest.practice.group.repository.GroupRepository;
import jparest.practice.group.repository.GroupUserRepository;
import jparest.practice.group.service.GroupService;
import jparest.practice.invite.domain.Invite;
import jparest.practice.invite.dto.GetWaitingInviteResponse;
import jparest.practice.invite.dto.InviteUserRequest;
import jparest.practice.invite.dto.InviteUserResponse;
import jparest.practice.invite.exception.InviteNotFoundException;
import jparest.practice.invite.repository.InviteRepository;
import jparest.practice.user.domain.User;
import jparest.practice.user.repository.UserRepository;
import jparest.practice.user.service.UserAuthService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static jparest.practice.common.fixture.GroupFixture.groupName1;
import static jparest.practice.common.fixture.UserFixture.*;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class NoTransactionalInviteServiceTest {
    
    private User firstUser;
    private User secondUser;
    private Group group;

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserAuthService userAuthService;

    @Autowired
    InviteService inviteService;

    @Autowired
    GroupService groupService;

    @Autowired
    GroupRepository groupRepository;

    @Autowired
    GroupUserRepository groupUserRepository;

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

    @AfterEach
    void clear() {
        inviteRepository.deleteAll();
        groupUserRepository.deleteAll();
        groupRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    public void 대기중인_초대리스트_조회_N_PLUS_1() throws Exception {

        //given
        User thirdUser = userAuthService.join(createThirdUser());
        CreateGroupRequest createGroupRequest2 = CreateGroupRequest.builder()
                .groupName(groupName1)
                .isPublic(true)
                .build();

        CreateGroupResponse group2 = groupService.createGroup(thirdUser, createGroupRequest2);

        InviteUserRequest inviteUserRequest1 = new InviteUserRequest(secondUser.getId(), group.getId());
        InviteUserRequest inviteUserRequest2 = new InviteUserRequest(secondUser.getId(), group2.getId());

        InviteUserResponse response1 = inviteService.inviteToGroup(firstUser, inviteUserRequest1);
        inviteService.inviteToGroup(thirdUser, inviteUserRequest2);

        Invite invite = findInviteById(response1.getInviteId());

        //when
        List<GetWaitingInviteResponse> inviteList = inviteService.getWaitingInviteList(secondUser);

        //then
        assertAll(
                () -> assertEquals(invite.getId(), inviteList.get(0).getInviteId()),
                () -> assertEquals(firstUser.getNickname(), inviteList.get(0).getNickName()),
                () -> assertEquals(groupName1, inviteList.get(1).getGroupName())
        );
    }

    private Group findGroupById(Long groupId) {
        return groupRepository.findById(groupId).orElseThrow(() -> new GroupNotFoundException("groupId = " + groupId));
    }

    private Invite findInviteById(Long inviteId) {
        return inviteRepository.findById(inviteId).orElseThrow(() -> new InviteNotFoundException("inviteId = " + inviteId));
    }
}
