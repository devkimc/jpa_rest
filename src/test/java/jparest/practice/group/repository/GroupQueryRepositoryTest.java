package jparest.practice.group.repository;

import jparest.practice.common.config.QueryDslConfig;
import jparest.practice.group.domain.Group;
import jparest.practice.group.domain.GroupUser;
import jparest.practice.group.domain.GroupUserType;
import jparest.practice.group.dto.SearchGroupListResponse;
import jparest.practice.user.domain.LoginType;
import jparest.practice.user.domain.User;
import jparest.practice.user.domain.UserType;
import jparest.practice.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.List;

import static jparest.practice.common.fixture.GroupFixture.groupName1;
import static jparest.practice.common.fixture.GroupFixture.groupName2;
import static jparest.practice.common.fixture.UserFixture.*;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(QueryDslConfig.class)
public class GroupQueryRepositoryTest {

    private User firstUser;
    private User secondUser;

    private Group firstGroup;
    private Group secondGroup;

    private GroupUser firstUserOfFirstGroup;
    private GroupUser secondUserOfFirstGroup;
    private GroupUser secondUserOfSecondGroup;

    @Autowired
    GroupRepository groupRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    GroupUserRepository groupUserRepository;

    @Autowired
    GroupQueryRepository groupQueryRepository;

    @BeforeEach
    void setUp() {

        // 1. 유저
        firstUser = User.builder()
                .socialUserId(socialUserId1)
                .loginType(LoginType.KAKAO)
                .userType(UserType.ROLE_GENERAL)
                .email(email1)
                .nickname(nickname1)
                .build();

        secondUser = User.builder()
                .socialUserId(socialUserId2)
                .loginType(LoginType.KAKAO)
                .userType(UserType.ROLE_GENERAL)
                .email(email2)
                .nickname(nickname2)
                .build();

        // 2. 그룹
        firstGroup = Group.builder()
                .groupName(groupName1)
                .isPublic(true)
                .build();

        secondGroup = Group.builder()
                .groupName(groupName2)
                .isPublic(false)
                .build();

        // 3. 그룹 유저
        firstUserOfFirstGroup = GroupUser.builder()
                .groupUserType(GroupUserType.ROLE_OWNER)
                .group(firstGroup)
                .user(firstUser)
                .build();

        secondUserOfFirstGroup = GroupUser.builder()
                .groupUserType(GroupUserType.ROLE_MEMBER)
                .group(firstGroup)
                .user(secondUser)
                .build();

        secondUserOfSecondGroup = GroupUser.builder()
                .groupUserType(GroupUserType.ROLE_OWNER)
                .group(secondGroup)
                .user(secondUser)
                .build();
    }

    @Test
    public void 그룹_검색_검색어를_입력하지_않을_시() throws Exception {

        //given
        userRepository.save(firstUser);
        userRepository.save(secondUser);

        groupRepository.save(firstGroup);
        groupRepository.save(secondGroup);

        groupUserRepository.save(firstUserOfFirstGroup);
        groupUserRepository.save(secondUserOfFirstGroup);
        groupUserRepository.save(secondUserOfSecondGroup);

        //when
        List<SearchGroupListResponse> groupList = groupQueryRepository.search("", "");

        //then
        assertAll(
                () -> assertEquals(1, groupList.size(), "공개된 그룹만 조회해야 한다."),
                () -> assertEquals(firstUser.getNickname(), groupList.get(0).getOwnerNickname(),
                        "그룹 소유자의 이름이 일치해야 한다."),
                () -> assertEquals(firstGroup.getGroupName(), groupList.get(0).getGroupName(),
                        "그룹 이름이 일치해야 한다.")
        );
    }

    @Test
    public void 그룹_검색_검색어를_입력_시() throws Exception {

        //given
        userRepository.save(firstUser);
        userRepository.save(secondUser);

        groupRepository.save(firstGroup);
        groupRepository.save(secondGroup);

        groupUserRepository.save(firstUserOfFirstGroup);
        groupUserRepository.save(secondUserOfFirstGroup);
        groupUserRepository.save(secondUserOfSecondGroup);

        //when
        List<SearchGroupListResponse> groupList = groupQueryRepository.search(
                "그룹", ""
        );

        //then
        assertAll(
                () -> assertEquals(1, groupList.size(), "공개된 그룹만 조회해야 한다."),
                () -> assertEquals(firstUser.getNickname(), groupList.get(0).getOwnerNickname(),
                        "그룹 소유자의 이름이 일치해야 한다."),
                () -> assertEquals(firstGroup.getGroupName(), groupList.get(0).getGroupName(),
                        "그룹 이름이 일치해야 한다.")
        );
    }
}