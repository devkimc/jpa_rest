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
}