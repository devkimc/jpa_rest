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
import jparest.practice.user.domain.User;
import jparest.practice.user.domain.UserType;
import jparest.practice.user.service.UserAuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;


import javax.servlet.http.Cookie;

import static jparest.practice.common.util.CookieUtils.createCookie;
import static jparest.practice.common.utils.ApiDocumentUtils.*;
import static jparest.practice.common.utils.GroupFixture.*;
import static jparest.practice.common.utils.UserFixture.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class GroupControllerTest extends ControllerTest {

    @Value("${domain.host}")
    private String domain;

    private User firstUser;

    private Cookie accessTokenCookie;
    private Cookie refreshTokenCookie;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    JwtService jwtService;

    @Autowired
    UserAuthService userAuthService;

    @Autowired
    GroupService groupService;

    @Autowired
    GroupRepository groupRepository;

    @BeforeEach
    void setUp() {
        firstUser = userAuthService.join(createFirstUser());

        String userId = String.valueOf(firstUser.getId());
        String accessToken = jwtService.createAccessToken(userId, UserType.ROLE_GENERAL.name());
        String refreshToken = jwtService.createRefreshToken(userId);

        accessTokenCookie = createCookie(TokenType.ACCESS_TOKEN.name(), accessToken, domain);
        refreshTokenCookie = createCookie(TokenType.REFRESH_TOKEN.name(), refreshToken, domain);
    }

    @Test
    @Transactional
    @DisplayName("그룹 생성")
    void createGroup() throws Exception {

        //given
        CreateGroupRequest createGroupRequest = new CreateGroupRequest(groupName1);
        String content = objectMapper.writeValueAsString(createGroupRequest);

        //when
        ResultActions result = mockMvc.perform(
                post("/api/groups")
                        .cookie(accessTokenCookie)
                        .cookie(refreshTokenCookie)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
        );

        //then
        result
                .andExpect(status().isOk())
                .andExpectAll(
                        jsonPath("$.result.groupName").value(groupName1)
                )
                .andDo(document("post-groups",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        responseFields(
                                fieldWithPath("success").description("성공 여부"),
                                fieldWithPath("result.id").description("그룹 아이디"),
                                fieldWithPath("result.groupName").description("그룹 이름")
                        )))
                .andDo(print());
    }

    @Test
    @Transactional
    @DisplayName("그룹 탈퇴")
    void withdrawGroup() throws Exception {

        //given
        Long groupId = groupService.createGroup(firstUser, groupName1).getId();

        //when
        ResultActions result = mockMvc.perform(
                delete("/api/groups/{groupId}/users", groupId)
                        .cookie(accessTokenCookie)
                        .cookie(refreshTokenCookie)
                        .contentType(MediaType.APPLICATION_JSON)
        );

        //then
        result
                .andExpect(status().isOk())
                .andExpectAll(
                        jsonPath("$.result").value(true)
                )
                .andDo(document("delete-groups-users",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        responseFields(
                                fieldWithPath("success").description("성공 여부"),
                                fieldWithPath("result").description("성공 여부")
                        )))
                .andDo(print());
    }

    @Test
    @Transactional
    @DisplayName("그룹 리스트 조회")
    void getGroupList() throws Exception {

        //given
        CreateGroupResponse response = groupService.createGroup(firstUser, groupName1);

        //when
        ResultActions result = mockMvc.perform(
                get("/api/groups")
                        .cookie(accessTokenCookie)
                        .cookie(refreshTokenCookie)
                        .contentType(MediaType.APPLICATION_JSON)
        );

        //then
        result
                .andExpect(status().isOk())
                .andExpectAll(
                        jsonPath("$.result.[0].groupId").value(response.getId()),
                        jsonPath("$.result.[0].groupName").value(groupName1),
                        jsonPath("$.result.[0].totalUsers").value(1)
                )
                .andDo(document("get-groups",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        responseFields(
                                fieldWithPath("success").description("성공 여부"),
                                fieldWithPath("result.[].groupId").description("그룹 아이디"),
                                fieldWithPath("result.[].groupName").description("그룹 이름"),
                                fieldWithPath("result.[].totalUsers").description("그룹 인원수")
                        )))
                .andDo(print());
    }

    private Group findGroupById(Long groupId) {
        return groupRepository.findById(groupId).orElseThrow(() -> new GroupNotFoundException("groupId = " + groupId));
    }


}
