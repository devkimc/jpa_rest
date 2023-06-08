package jparest.practice.user.service;

import jparest.practice.user.domain.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static jparest.practice.common.utils.fixture.UserFixture.createFirstUser;
import static jparest.practice.common.utils.fixture.UserFixture.socialUserId1;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
public class UserServiceTest {

    @Autowired
    UserAuthService userAuthService;

    @Test
    public void UUID_회원가입() throws Exception {
        
        //given

        //when
        User joinUser = userAuthService.join(createFirstUser());

        //then
        assertEquals(socialUserId1, joinUser.getSocialUserId(), "가입한 유저의 소셜 ID 가 일치해야 한다.");
    }
            

//    @Test
//    public void 회원가입() throws Exception {
//        //given
//        User user = new User();
//        user.setLoginId("loginId");
//        user.setPassword("1234");
//        user.setName("덩어리");
//        user.setEmail("test@test.com");
//
//        //when
//        User saveUser = userAuthService.join(user);
//
//        //then
//        assertEquals(user.getLoginId(), saveUser.getLoginId());
//    }
//
//    @Test(expected = ExistLoginIdException.class)
//    public void 중복_회원_예외() throws Exception {
//
//        //given
//        User user1 = new User();
//        user1.setLoginId("loginId");
//        user1.setPassword("1234");
//
//        User user2 = new User();
//        user2.setLoginId("loginId");
//        user2.setPassword("4321");
//
//        //when
//        userAuthService.join(user1);
//        userAuthService.join(user2);
//
//        //then
//        fail("중복_회원_예외가 발생해야 한다.");
//    }
//
//    @Test
//    public void 로그인() throws Exception {
//
//        //given
//        User user = new User();
//        user.setLoginId("loginId");
//        user.setPassword("real_password");
//        userAuthService.join(user);
//
//        //when
//        UserLoginResponse loginUser = userAuthService.login(user.getLoginId(), user.getPassword());
//
//        //then
//        assertNotNull(loginUser.getTokenDto());
//    }
//
//    @Test(expected = LoginFailException.class)
//    public void 비밀번호_불일치_예외() throws Exception {
//
//        //given
//        User user = new User();
//        user.setLoginId("loginId");
//        user.setPassword("real_password");
//        userAuthService.join(user);
//
//        //when
//        userAuthService.login("loginId", "fake_password");
//
//        //then
//        fail("비밀번호_불일치_예외가 발생해야 한다.");
//    }

}