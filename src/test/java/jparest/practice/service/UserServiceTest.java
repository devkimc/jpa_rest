package jparest.practice.service;

import jparest.practice.member.domain.User;
import jparest.practice.member.repository.UserRepository;
import jparest.practice.member.service.UserService;
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
        user.setLoginId("loginID");
        user.setPassword("1234");
        user.setName("덩어리");
        user.setEmail("test@test.com");

        //when
        User saveUser = userService.join(user);

        //then
        assertEquals(user.getLoginId(), saveUser.getLoginId());
    }

    @Test(expected = IllegalStateException.class)
    public void 중복_회원_예외() throws Exception {

        //given
        User user1 = new User();
        user1.setLoginId("아이디");
        user1.setPassword("1234");
        user1.setName("덩어리름");
        user1.setEmail("test@test.com");

        User user2 = new User();
        user2.setLoginId("아이디");
        user2.setPassword("4321");
        user2.setName("덩어리름");
        user2.setEmail("test@test.com");

        //when
        userService.join(user1);
        userService.join(user2);

        //then
        fail("예외가 발생해야 한다.");
    }
}