package jparest.practice.service;

import jparest.practice.group.domain.Group;
import jparest.practice.group.exception.GroupNotFoundException;
import jparest.practice.group.repository.GroupRepository;
import jparest.practice.group.repository.UserGroupRepository;
import jparest.practice.group.service.GroupService;
import jparest.practice.user.domain.LoginType;
import jparest.practice.user.domain.User;
import jparest.practice.user.dto.SocialJoinRequest;
import jparest.practice.user.service.UserAuthService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

//@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class GroupServiceTest {

    @Autowired
    UserAuthService userAuthService;
    @Autowired
    GroupService groupService;
    @Autowired
    GroupRepository groupRepository;
    @Autowired
    UserGroupRepository userGroupRepository;

    @Test
    public void 그룹생성() throws Exception {

        //given
        String groupName = "첫 그룹";
        String socialUserId = "123123";
        String email = "eee@www.aaaa";
        String nickname = "닉네임";
        LoginType loginType = LoginType.KAKAO;

        SocialJoinRequest socialJoinRequest = new SocialJoinRequest(socialUserId, email, nickname, loginType);
        User joinUser = userAuthService.join(socialJoinRequest);

        //when
        Long saveGroupId = groupService.addGroup(joinUser, groupName);
        Optional<Group> findGroup = groupRepository.findById(saveGroupId);
        System.out.println(findGroup.get().getUserGroups().toString());

        //then
        findGroup.orElseThrow(() -> new GroupNotFoundException("groupId = " + saveGroupId));
        String saveGroupName = findGroup.get().getGroupName();

        assertEquals(groupName, saveGroupName,"생성한 그룹의 이름이 일치해야 한다.");

//        assertAll("userGroup",
//                () -> assertEquals(saveGroupId, )
//                );

//        Assert.assertEquals("생성한 그룹의 이름이 일치해야 한다.", saveGroupName, groupName);

//        findGroup.orElseGet(() -> findGroup.get().)

//        //given
//        Member member = createMember();
//
//        //when
//        Long groupId = groupService.makeGroup(member.getId(), "반민초파");
//
//        //then
//        Group getGroup = groupRepository.findOne(groupId);
//
//        Assert.assertEquals("생성한 그룹 수가 정확해야 한다.",
//                1, getGroup.getGroupMembers().size());
//
//        Assert.assertEquals("그룹 생성자가 정확해야 한다..",
//                member.getId(), getGroup.getCreateUserId());

    }


}
