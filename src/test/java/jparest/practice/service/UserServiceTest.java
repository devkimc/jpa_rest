package jparest.practice.service;

import jparest.practice.user.domain.User;
import jparest.practice.user.dto.UserLoginResponse;
import jparest.practice.user.exception.ExistLoginIdException;
import jparest.practice.user.repository.UserRepository;
import jparest.practice.user.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class UserServiceTest {

    @Autowired
    UserService userService;
    @Autowired
    UserRepository userRepository;

    @Test
    public void 회원가입() throws Exception {
        //given
        User user = new User();
        user.setLoginId("loginId");
        user.setPassword("1234");
        user.setName("덩어리");
        user.setEmail("test@test.com");

        //when
        User saveUser = userService.join(user);

        //then
        assertEquals(user.getLoginId(), saveUser.getLoginId());
    }

    @Test(expected = ExistLoginIdException.class)
    public void 중복_회원_예외() throws Exception {

        //given
        User user1 = new User();
        user1.setLoginId("loginId");
        user1.setPassword("1234");

        User user2 = new User();
        user2.setLoginId("loginId");
        user2.setPassword("4321");

        //when
        userService.join(user1);
        userService.join(user2);

        //then
        fail("중복_회원_예외가 발생해야 한다.");
    }

    @Test
    public void 로그인() throws Exception {

        //given
        User user = new User();
        user.setLoginId("loginId");
        user.setPassword("real_password");
        userService.join(user);

        //when
        UserLoginResponse loginUser = userService.login(user.getLoginId(), user.getPassword());

        //then
        assertNotNull(loginUser.getTokenDto());
    }

    @Test(expected = IllegalArgumentException.class)
    public void 비밀번호_불일치_예외() throws Exception {

        //given
        User user = new User();
        user.setLoginId("loginId");
        user.setPassword("real_password");
        userService.join(user);

        //when
        userService.login("loginId", "fake_password");

        //then
        fail("비밀번호_불일치_예외가 발생해야 한다.");
    }

}