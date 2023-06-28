package jparest.practice.group.repository;

import jparest.practice.common.config.QueryDslConfig;
import jparest.practice.group.domain.Group;
import jparest.practice.group.domain.GroupUser;
import jparest.practice.group.domain.GroupUserType;
import jparest.practice.user.domain.LoginType;
import jparest.practice.user.domain.User;
import jparest.practice.user.domain.UserType;
import jparest.practice.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.List;

import static jparest.practice.common.fixture.GroupFixture.groupName1;
import static jparest.practice.common.fixture.UserFixture.*;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(QueryDslConfig.class)
public class GroupQueryRepositoryTest {

    @Autowired
    GroupRepository groupRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    GroupUserRepository groupUserRepository;

    @Autowired
    GroupQueryRepository groupQueryRepository;

    @Test
    public void 그룹_검색() throws Exception {

        //given
        Group group = Group.builder()
                .groupName(groupName1)
                .isPublic(true)
                .build();

        User user = User.builder()
                .socialUserId(socialUserId1)
                .loginType(LoginType.KAKAO)
                .userType(UserType.ROLE_GENERAL)
                .email(email1)
                .nickname(nickname1)
                .build();

        GroupUser groupUser = GroupUser.builder()
                .id(1L)
                .groupUserType(GroupUserType.ROLE_OWNER)
                .group(group)
                .user(user)
                .build();

        userRepository.save(user);
        groupRepository.save(group);
        groupUserRepository.save(groupUser);

        //when
        List<Group> groupList = groupQueryRepository.search("", "").get();


        //then

        assertAll(
                () -> assertEquals(1, groupList.size())
        );

    }

}