package jparest.practice.service;

import jparest.practice.group.domain.Group;
import jparest.practice.group.dto.CreateGroupResponse;
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

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class GroupServiceTest {

    @Autowired
    UserAuthService userAuthService;

    @Autowired
    GroupService groupService;

    @Autowired
    GroupRepository groupRepository;

    User joinUser;

    @BeforeEach
    void setUp() {
        String socialUserId = "123123";
        String email = "eee@www.aaaa";
        String nickname = "닉네임";
        LoginType loginType = LoginType.KAKAO;

        SocialJoinRequest socialJoinRequest = new SocialJoinRequest(socialUserId, email, nickname, loginType);
        joinUser = userAuthService.join(socialJoinRequest);
    }


    @Test
    public void 그룹생성() throws Exception {

        //given
        String groupName = "첫 그룹";

        //when
        Group group = groupService.createGroup(joinUser, groupName);

        //then
        String saveGroupName = group.getGroupName();

        assertEquals(groupName, saveGroupName,"생성한 그룹의 이름이 일치해야 한다.");
    }

    @Test
    @DisplayName("마지막 유저가 탈퇴시 그룹은 삭제되어야 한다.")
    public void 그룹탈퇴_마지막_유저() throws Exception {

        //given
        String groupName = "첫 그룹";
        Long saveGroupId = groupService.createGroup(joinUser, groupName).getId();

        //when
        groupService.withdrawGroup(joinUser, saveGroupId);

        //then
        assertThrows(GroupNotFoundException.class, () -> {
            groupRepository.findById(saveGroupId)
                    .orElseThrow(() -> new GroupNotFoundException("groupId = " + saveGroupId));
        });
    }

    private Group findGroup(Long groupId) {
        return groupRepository.findById(groupId).orElseThrow(() -> new GroupNotFoundException("groupId = " + groupId));
    }
}
