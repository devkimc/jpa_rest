package jparest.practice.service;

import jparest.practice.user.domain.LoginType;
import jparest.practice.user.domain.User;
import jparest.practice.user.dto.SocialJoinRequest;
import jparest.practice.user.repository.UserRepository;
import jparest.practice.user.service.UserAuthService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class UserServiceTest {

    @Autowired
    UserAuthService userAuthService;
    @Autowired
    UserRepository userRepository;

    @Test
    public void UUID_회원가입() throws Exception {
        
        //given
        String socialUserId = "123123";
        String email = "eee@www.aaaa";
        String nickname = "닉네임";
        LoginType loginType = LoginType.KAKAO;

        //when
        User joinUser = userAuthService.join(new SocialJoinRequest(socialUserId, email, nickname, loginType));

        //then
        assertEquals(socialUserId, joinUser.getSocialUserId(), "가입한 유저의 소셜 ID 가 일치해야 한다.");
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