package jparest.practice.group.service;

import jparest.practice.group.domain.Group;
import jparest.practice.group.domain.GroupUser;
import jparest.practice.group.domain.GroupUserType;
import jparest.practice.group.dto.*;
import jparest.practice.group.exception.GroupNotFoundException;
import jparest.practice.group.exception.GroupUserNotFoundException;
import jparest.practice.group.repository.GroupQueryRepository;
import jparest.practice.group.repository.GroupRepository;
import jparest.practice.group.repository.GroupUserRepository;
import jparest.practice.invite.domain.Invite;
import jparest.practice.invite.domain.InviteStatus;
import jparest.practice.invite.dto.InviteUserRequest;
import jparest.practice.invite.dto.InviteUserResponse;
import jparest.practice.invite.dto.ProcessInvitationRequest;
import jparest.practice.invite.exception.InviteNotFoundException;
import jparest.practice.invite.repository.InviteRepository;
import jparest.practice.invite.service.InviteService;
import jparest.practice.rest.domain.GroupRest;
import jparest.practice.rest.exception.GroupRestNotFoundException;
import jparest.practice.rest.repository.GroupRestRepository;
import jparest.practice.rest.service.FavoriteRestaurantService;
import jparest.practice.user.domain.User;
import jparest.practice.user.service.UserAuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

import static jparest.practice.common.fixture.GroupFixture.groupName1;
import static jparest.practice.common.fixture.GroupFixture.groupName2;
import static jparest.practice.common.fixture.RestFixture.*;
import static jparest.practice.common.fixture.UserFixture.createFirstUser;
import static jparest.practice.common.fixture.UserFixture.createSecondUser;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class GroupServiceTest {

    private User firstUser;
    private User secondUser;

    private CreateGroupRequest createFirstGroupRequest;
    private CreateGroupRequest createSecondGroupRequest;

    @Autowired
    UserAuthService userAuthService;

    @Autowired
    GroupRepository groupRepository;

    @Autowired
    GroupService groupService;

    @Autowired
    GroupUserRepository groupUserRepository;

    @Autowired
    GroupQueryRepository groupQueryRepository;

    @Autowired
    InviteRepository inviteRepository;

    @Autowired
    InviteService inviteService;

    @Autowired
    FavoriteRestaurantService favoriteRestaurantService;

    @Autowired
    GroupRestRepository groupRestRepository;

    @BeforeEach
    void setUp() {
        firstUser = userAuthService.join(createFirstUser());

        createFirstGroupRequest = CreateGroupRequest.builder()
                .groupName(groupName1)
                .isPublic(true)
                .build();

        createSecondGroupRequest = CreateGroupRequest.builder()
                .groupName(groupName2)
                .isPublic(false)
                .build();
    }

    @Test
    public void 그룹생성() throws Exception {

        //given

        //when
        CreateGroupResponse response = groupService.createGroup(firstUser, createFirstGroupRequest);

        Group group = findGroup(response.getId());
        GroupUser groupUser = findGroupUser(firstUser.getId(), group.getId());

        //then
        assertAll(
                () -> assertEquals(groupName1, group.getGroupName(), "생성한 그룹의 이름이 일치해야 한다."),
                () -> assertEquals(GroupUserType.ROLE_OWNER, groupUser.getGroupUserType(), "그룹 생성자의 역할은 OWNER 이다.")
        );
    }

    @Test
    public void 그룹탈퇴() throws Exception {

        //given
        Long saveGroupId = groupService.createGroup(firstUser, createFirstGroupRequest).getId();

        //when
        groupService.withdrawGroup(firstUser, saveGroupId);

        //then
        assertThrows(GroupUserNotFoundException.class, () -> findGroupUser(firstUser.getId(), saveGroupId));
    }

    @Test
    public void 마지막_그룹원이_탈퇴시_연관된_고아객체를_모두_삭제한다() throws Exception {

        //given
        Long saveGroupId = groupService.createGroup(firstUser, createFirstGroupRequest).getId();
        secondUser = userAuthService.join(createSecondUser());

        InviteUserResponse response = inviteService.inviteToGroup(firstUser, new InviteUserRequest(secondUser.getId(), saveGroupId));

        favoriteRestaurantService.addFavRest(firstUser, restId1, createAddFavoriteRestRequest(saveGroupId, restName1));
        GroupRest groupRest = findGroup(saveGroupId).getGroupRests().get(0);

        // when
        groupService.withdrawGroup(firstUser, saveGroupId);

        //then
        assertAll(
                () -> assertThrows(GroupUserNotFoundException.class, () -> findGroupUser(firstUser.getId(), saveGroupId)),
                () -> assertThrows(InviteNotFoundException.class, () -> findInvite(response.getInviteId())),
                () -> assertThrows(GroupNotFoundException.class, () -> findGroup(saveGroupId)),
                () -> assertThrows(GroupRestNotFoundException.class, () -> findGroupRest(groupRest.getId()))
        );
    }

    @Test
    public void 그룹_리스트_조회() throws Exception {

        //given

        Long id1 = groupService.createGroup(firstUser, createFirstGroupRequest).getId();
        Long id2 = groupService.createGroup(firstUser, createSecondGroupRequest).getId();

        Group group1 = findGroup(id1);
        Group group2 = findGroup(id2);

        //when
        List<GetGroupUserResponse> groupUserList = groupService.getGroupUserList(firstUser);
        GetGroupUserResponse result1 = groupUserList.get(0);
        GetGroupUserResponse result2 = groupUserList.get(1);

        //then
        assertAll(
                () -> assertEquals(group1.getId(), result1.getGroupId()),
                () -> assertEquals(group1.getGroupName(), result1.getGroupName()),
                () -> assertEquals(group1.getUserCount(), result1.getTotalUsers()),
                () -> assertEquals(group2.getId(), result2.getGroupId()),
                () -> assertEquals(group2.getGroupName(), result2.getGroupName()),
                () -> assertEquals(group2.getUserCount(), result2.getTotalUsers())
        );
    }

    @Test
    public void 그룹_소유자_변경() throws Exception {

        //given
        Long saveGroupId = groupService.createGroup(firstUser, createFirstGroupRequest).getId();
        secondUser = userAuthService.join(createSecondUser());

        InviteUserRequest inviteUserRequest = new InviteUserRequest(secondUser.getId(), saveGroupId);
        InviteUserResponse inviteUserResponse = inviteService.inviteToGroup(firstUser, inviteUserRequest);

        inviteService.processInvitation(inviteUserResponse.getInviteId(), secondUser,
                new ProcessInvitationRequest(InviteStatus.ACCEPT));

        //when
        ChangeOwnerRequest changeOwnerRequest = ChangeOwnerRequest.builder()
                .successorId(secondUser.getId())
                .build();

        ChangeOwnerResponse changeOwnerResponse = groupService.changeOwner(
                firstUser, saveGroupId, changeOwnerRequest);

        //then
        assertAll(
                () -> assertEquals(GroupUserType.ROLE_MEMBER, firstUser.getGroupUsers().get(0).getGroupUserType(),
                        "전임자는 소유자 변경 후 MEMBER 역할이 되야 한다."),
                () -> assertEquals(GroupUserType.ROLE_OWNER, secondUser.getGroupUsers().get(0).getGroupUserType(),
                        "후임자는 소유자 변경 후 OWNER 역할이 되야 한다"),
                () -> assertEquals(secondUser.getNickname(), changeOwnerResponse.getOwnerNickname(),
                        "후임자의 닉네임이 응닶값과 일치해야 한다.")
        );
    }

    @Test
    public void 그룹_이름만_검색_시() throws Exception {

        //given
        groupService.createGroup(firstUser, createFirstGroupRequest); // public
        groupService.createGroup(firstUser, createSecondGroupRequest); // private

        //when
        List<SearchGroupListResponse> resultList = groupQueryRepository.search("그룹", "");

        //then
        assertAll(
                () -> assertEquals(1, resultList.size(), "공개된 그룹만 조회해야 한다."),
                () -> assertEquals(firstUser.getNickname(), resultList.get(0).getOwnerNickname(),
                        "그룹 소유자의 닉네임 같아야 한다."),
                () -> assertEquals(groupName1, resultList.get(0).getGroupName(),
                        "그룹의 이름은 같아야 한다.")
        );
    }

    private Group findGroup(Long groupId) {
        return groupRepository.findById(groupId)
                .orElseThrow(() -> new GroupNotFoundException("groupId = " + groupId));
    }

    private GroupUser findGroupUser(UUID userId, Long groupId) {
        return groupUserRepository.findByUserIdAndGroupId(userId, groupId)
                .orElseThrow(() -> new GroupUserNotFoundException("userId = " + userId + ", groupId = " + groupId));
    }

    private Invite findInvite(Long inviteId) {
        return inviteRepository.findById(inviteId)
                .orElseThrow(() -> new InviteNotFoundException("inviteId = " + inviteId));
    }

    private GroupRest findGroupRest(Long groupRestId) {
        return groupRestRepository.findById(groupRestId)
                .orElseThrow(() -> new GroupRestNotFoundException("groupRestId = " + groupRestId));
    }
}
