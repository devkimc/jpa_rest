package jparest.practice.service;

import jparest.practice.group.domain.Group;
import jparest.practice.group.dto.CreateGroupResponse;
import jparest.practice.group.dto.GetUserGroupResponse;
import jparest.practice.group.exception.GroupNotFoundException;
import jparest.practice.group.repository.GroupRepository;
import jparest.practice.group.service.GroupService;
import jparest.practice.user.domain.LoginType;
import jparest.practice.user.domain.User;
import jparest.practice.user.dto.SocialJoinRequest;
import jparest.practice.user.service.UserAuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class GroupServiceTest {
    private final String socialUserId1 = "123123";
    private final String email1 = "eee@www.aaaa";
    private final String nickname1 = "유저1";
    private final LoginType loginType1 = LoginType.KAKAO;

    private final String socialUserId2 = "234234";
    private final String email2 = "eee@www.bbb";
    private final String nickname2 = "유저2";
    private final LoginType loginType2 = LoginType.KAKAO;

    @Autowired
    EntityManager entityManager;

    @Autowired
    UserAuthService userAuthService;

    @Autowired
    GroupService groupService;

    @Autowired
    GroupRepository groupRepository;

    User joinUser1;
    User joinUser2;

    @BeforeEach
    void setUp() {
        this.joinUser1 = joinSetup(new SocialJoinRequest(socialUserId1, email1, nickname1, loginType1));
//        this.joinUser2 = joinSetup(new SocialJoinRequest(socialUserId2, email2, nickname2, loginType2));
    }


    @Test
    public void 그룹생성() throws Exception {

        //given
        String groupName = "첫 그룹";

        //when
        Group group = groupService.createGroup(joinUser1, groupName);

        //then
        String saveGroupName = group.getGroupName();

        assertEquals(groupName, saveGroupName,"생성한 그룹의 이름이 일치해야 한다.");
    }

    @Test
    @DisplayName("마지막 유저가 탈퇴시 그룹은 삭제되어야 한다.")
    public void 그룹탈퇴_마지막_유저() throws Exception {

        //given
        String groupName = "첫 그룹";
        Long saveGroupId = groupService.createGroup(joinUser1, groupName).getId();

        //when
        groupService.withdrawGroup(joinUser1, saveGroupId);

        //then
        assertThrows(GroupNotFoundException.class, () -> {
            groupRepository.findById(saveGroupId)
                    .orElseThrow(() -> new GroupNotFoundException("groupId = " + saveGroupId));
        });
    }

    @Test
    public void 그룹_리스트_조회() throws Exception {

        //given
        String groupName1 = "첫 그룹";
        String groupName2 = "두번째 그룹";
        Long id1 = groupService.createGroup(joinUser1, groupName1).getId();
        Long id2 = groupService.createGroup(joinUser1, groupName2).getId();

        Group group1 = findGroup(id1);
        Group group2 = findGroup(id2);

        //when
        List<GetUserGroupResponse> userGroupList = groupService.getUserGroupList(joinUser1);
        GetUserGroupResponse result1 = userGroupList.get(0);
        GetUserGroupResponse result2 = userGroupList.get(1);

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

    private Group findGroup(Long groupId) {
        return groupRepository.findById(groupId).orElseThrow(() -> new GroupNotFoundException("groupId = " + groupId));
    }

    private User joinSetup(SocialJoinRequest socialJoinRequest) {
        return userAuthService.join(socialJoinRequest);
    }
}
