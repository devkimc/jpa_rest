package jparest.practice.service;

import jparest.practice.group.domain.Group;
import jparest.practice.group.domain.UserGroup;
import jparest.practice.group.service.GroupService;
import jparest.practice.invite.domain.Invite;
import jparest.practice.invite.domain.InviteStatus;
import jparest.practice.invite.dto.InviteStatusPatchRequest;
import jparest.practice.invite.exception.ExistInviteForUserException;
import jparest.practice.invite.repository.InviteRepository;
import jparest.practice.invite.service.InviteService;
import jparest.practice.user.domain.LoginType;
import jparest.practice.user.domain.User;
import jparest.practice.user.dto.SocialJoinRequest;
import jparest.practice.user.service.UserAuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class InviteServiceTest {

    private final String socialUserId1 = "123123";
    private final String email1 = "eee@www.aaaa";
    private final String nickname1 = "유저1";
    private final LoginType loginType1 = LoginType.KAKAO;

    private final String socialUserId2 = "234234";
    private final String email2 = "eee@www.bbb";
    private final String nickname2 = "유저2";
    private final LoginType loginType2 = LoginType.KAKAO;

    private final String groupName = "유저 1의 나라";

    @Autowired
    UserAuthService userAuthService;

    @Autowired
    InviteService inviteService;

    @Autowired
    GroupService groupService;

    User joinUser1;
    User joinUser2;

    Group group1;

    @BeforeEach
    void setUp() {
        joinUser1 = joinSetup(new SocialJoinRequest(socialUserId1, email1, nickname1, loginType1));
        joinUser2 = joinSetup(new SocialJoinRequest(socialUserId2, email2, nickname2, loginType2));

        group1 = groupService.createGroup(joinUser1, groupName);
    }

    @Test
    public void 그룹초대() throws Exception {

        //given
        List<UserGroup> userGroups = joinUser1.getUserGroups();

        //when
        Invite invite = inviteService.inviteToGroup(group1.getId(), joinUser1, joinUser2.getId());

        //then
        assertAll(
                () -> assertEquals(InviteStatus.WAITING, invite.getInviteStatus()), // 초대 상태
                () -> assertEquals(userGroups.get(0), invite.getSendUserGroup()), // 초대한 유저
                () -> assertEquals(joinUser2, invite.getRecvUser()) // 초대받은 유저
        );
    }

    @Test
    public void 그룹초대_승낙() throws Exception {
        //given
        Invite invite = inviteService.inviteToGroup(group1.getId(), joinUser1, joinUser2.getId());
        InviteStatusPatchRequest inviteStatusPatchRequest = new InviteStatusPatchRequest(InviteStatus.ACCEPT);

        //when
        inviteService.procInvitation(invite.getId(), joinUser2, inviteStatusPatchRequest);
        Group group = invite.getSendUserGroup().getGroup();

        //then
        assertAll(
                ()-> assertEquals(InviteStatus.ACCEPT, invite.getInviteStatus()), // 초대 승낙
                ()-> assertEquals(group.getUserGroups().get(1).getUser(), joinUser2) // 그룹원 추가
        );
    }

    @Test
    public void 그룹초대_거절() throws Exception {
        //given
        Invite invite = inviteService.inviteToGroup(group1.getId(), joinUser1, joinUser2.getId());
        InviteStatusPatchRequest inviteStatusPatchRequest = new InviteStatusPatchRequest(InviteStatus.REJECT);

        //when
        inviteService.procInvitation(invite.getId(), joinUser2, inviteStatusPatchRequest);

        //then
        assertEquals(InviteStatus.REJECT, invite.getInviteStatus());
    }

    @Test
    public void 그룹초대_취소() throws Exception {
        //given
        Invite invite = inviteService.inviteToGroup(group1.getId(), joinUser1, joinUser2.getId());
        InviteStatusPatchRequest inviteStatusPatchRequest = new InviteStatusPatchRequest(InviteStatus.CANCEL);

        //when
        inviteService.procInvitation(invite.getId(), joinUser1, inviteStatusPatchRequest);

        //then
        assertEquals(InviteStatus.CANCEL, invite.getInviteStatus());
    }

    @Test
    public void 그룹초대_중복() throws Exception {
        //given

        //when
        inviteService.inviteToGroup(group1.getId(), joinUser1, joinUser2.getId());

        //then
        assertThrows(ExistInviteForUserException.class, () ->
                inviteService.inviteToGroup(group1.getId(), joinUser1, joinUser2.getId()));
    }

    private User joinSetup(SocialJoinRequest socialJoinRequest) {
        return userAuthService.join(socialJoinRequest);
    }
}
