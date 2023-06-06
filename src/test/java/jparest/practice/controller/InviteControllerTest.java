package jparest.practice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jparest.ControllerTest;
import jparest.practice.auth.jwt.JwtService;
import jparest.practice.auth.jwt.TokenType;
import jparest.practice.group.domain.Group;
import jparest.practice.group.dto.CreateGroupRequest;
import jparest.practice.group.dto.CreateGroupResponse;
import jparest.practice.group.exception.GroupNotFoundException;
import jparest.practice.group.repository.GroupRepository;
import jparest.practice.group.service.GroupService;
import jparest.practice.invite.controller.InviteController;
import jparest.practice.invite.dto.InviteUserRequest;
import jparest.practice.invite.dto.InviteUserResponse;
import jparest.practice.invite.service.InviteService;
import jparest.practice.user.domain.User;
import jparest.practice.user.domain.UserType;
import jparest.practice.user.service.UserAuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.Cookie;

import java.util.Optional;

import static jparest.practice.common.util.CookieUtils.createCookie;
import static jparest.practice.common.utils.ApiDocumentUtils.getDocumentRequest;
import static jparest.practice.common.utils.ApiDocumentUtils.getDocumentResponse;
import static jparest.practice.common.utils.GroupFixture.groupName1;
import static jparest.practice.common.utils.UserFixture.createFirstUser;
import static jparest.practice.common.utils.UserFixture.createSecondUser;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
public class InviteControllerTest extends ControllerTest {

    @Value("${domain.host}")
    private String domain;

    private User sendUser;
    private User recvUser;
    private Group group;

    private Cookie accessTokenCookie;
    private Cookie refreshTokenCookie;

    @Autowired
    JwtService jwtService;

    @Autowired
    UserAuthService userAuthService;

    @Autowired
    GroupService groupService;

    @Autowired
    GroupRepository groupRepository;

    @MockBean
    InviteService inviteService;

    private CreateGroupResponse groupResponse;
    private Group group1;

    @BeforeEach
    void setUp() {
        sendUser = userAuthService.join(createFirstUser());
        recvUser = userAuthService.join(createSecondUser());

        String userId = String.valueOf(sendUser.getId());
        String accessToken = jwtService.createAccessToken(userId, UserType.ROLE_GENERAL.name());
        String refreshToken = jwtService.createRefreshToken(userId);

        accessTokenCookie = createCookie(TokenType.ACCESS_TOKEN.name(), accessToken, domain);
        refreshTokenCookie = createCookie(TokenType.REFRESH_TOKEN.name(), refreshToken, domain);

        groupResponse = groupService.createGroup(sendUser, groupName1);
        Long id = groupResponse.getId();
        group1 = groupRepository.findById(id).get();
    }

    @Test
    @DisplayName("유저 초대")
    void inviteToGroup() throws Exception {

        //given
        InviteUserRequest inviteUserRequest = new InviteUserRequest(recvUser.getId(), 1L);

        when(inviteService.inviteToGroup(any(), any()))
                .thenReturn(new InviteUserResponse(1L));

        //when
        ResultActions result = mockMvc.perform(
                post("/api/invites")
                        .cookie(accessTokenCookie)
                        .cookie(refreshTokenCookie)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createJson(inviteUserRequest))
        );

        //then
        result
                .andExpect(status().isOk())
                .andExpectAll(
                        jsonPath("$.result.inviteId").exists()
                )
                .andDo(document("post-invites",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        responseFields(
                                fieldWithPath("success").description("성공 여부"),
                                fieldWithPath("result.inviteId").description("초대 아이디")
                        )))
                .andDo(print());
    }

    private Group findGroupById(Long groupId) {
        return groupRepository.findById(groupId).orElseThrow(() -> new GroupNotFoundException("groupId = " + groupId));
    }
}
