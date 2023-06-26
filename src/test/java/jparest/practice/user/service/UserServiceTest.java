package jparest.practice.user.service;

import jparest.practice.user.domain.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static jparest.practice.common.utils.fixture.UserFixture.*;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
public class UserServiceTest {

    @Autowired
    UserAuthService userAuthService;

    @Autowired
    UserInfoService userInfoService;

    @Test
    public void UUID_회원가입() throws Exception {
        
        //given

        //when
        User joinUser = userAuthService.join(createFirstUser());

        //then
        assertEquals(socialUserId1, joinUser.getSocialUserId(), "가입한 유저의 소셜 ID 가 일치해야 한다.");
    }

    @Test
    public void 닉네임_중복검사() throws Exception {

        //given
        userAuthService.join(createFirstUser());

        //when
        Boolean resultOfJoinUserNickName = userInfoService.chkNickNameDuplicate(nickname1);
        Boolean resultOfNotJoinUserNickName = userInfoService.chkNickNameDuplicate(nickname2);

        //then
        assertAll(
                () -> assertEquals(true, resultOfJoinUserNickName, "가입한 유저의 닉네임은 TRUE 를 반환해야 한다."),
                () -> assertEquals(false, resultOfNotJoinUserNickName, "가입하지 않은 닉네임은 FALSE 를 반환해야 한다.")
        );
    }
}