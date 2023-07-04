package jparest.practice.common.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jparest.practice.auth.jwt.JwtFilter;
import jparest.practice.group.controller.GroupController;
import jparest.practice.group.service.GroupService;
import jparest.practice.invite.controller.InviteController;
import jparest.practice.invite.service.InviteService;
import jparest.practice.rest.controller.FavoriteRestaurantController;
import jparest.practice.rest.controller.RankingRestaurantController;
import jparest.practice.rest.service.FavoriteRestaurantService;
import jparest.practice.rest.service.RankingRestaurantService;
import jparest.practice.subscription.controller.SubscriptionController;
import jparest.practice.subscription.service.SubscriptionService;
import jparest.practice.user.controller.UserAuthController;
import jparest.practice.user.controller.UserInfoController;
import jparest.practice.user.service.UserAuthService;
import jparest.practice.user.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest({
        UserAuthController.class,
        UserInfoController.class,
        GroupController.class,
        InviteController.class,
        FavoriteRestaurantController.class,
        RankingRestaurantController.class,
        SubscriptionController.class
})
public abstract class ControllerTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @MockBean
    protected UserAuthService userAuthService;

    @MockBean
    protected UserInfoService userInfoService;

    @MockBean
    protected GroupService groupService;

    @MockBean
    protected InviteService inviteService;

    @MockBean
    protected RankingRestaurantService rankingRestaurantService;

    @MockBean
    protected FavoriteRestaurantService favoriteRestaurantService;

    @MockBean
    protected SubscriptionService subscriptionService;

    @MockBean
    JwtFilter jwtFilter;

    protected String createJson(Object dto) throws JsonProcessingException {
        return objectMapper.writeValueAsString(dto);
    }
}
