package jparest.practice.common.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jparest.practice.auth.jwt.JwtFilter;
import jparest.practice.group.controller.GroupController;
import jparest.practice.group.service.GroupService;
import jparest.practice.invite.controller.InviteController;
import jparest.practice.invite.service.InviteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest({
        GroupController.class,
        InviteController.class
})
public abstract class ControllerTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @MockBean
    protected GroupService groupService;

    @MockBean
    protected InviteService inviteService;

    @MockBean
    JwtFilter jwtFilter;

    protected String createJson(Object dto) throws JsonProcessingException {
        return objectMapper.writeValueAsString(dto);
    }
}
