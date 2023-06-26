package jparest.practice.user.controller;

import jparest.practice.auth.jwt.JwtService;
import jparest.practice.common.utils.RestDocsTestSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;

@AutoConfigureMockMvc(addFilters = false)
public class UserInfoControllerTest extends RestDocsTestSupport {

    private final String USER_API = "/api/user";

    @MockBean
    JwtService jwtService;

    @Test
    @DisplayName("닉네임 중복 체크")
    public void get_nickname_duplicate() throws Exception {
    }
}
