package jparest.practice.controller;

import jparest.SpringContainerTest;
import jparest.practice.auth.jwt.JwtService;
import jparest.practice.common.utils.GroupFixture;
import jparest.practice.common.utils.UserFixture;
import jparest.practice.group.domain.Group;
import jparest.practice.group.dto.CreateGroupResponse;
import jparest.practice.group.exception.GroupNotFoundException;
import jparest.practice.group.repository.GroupRepository;
import jparest.practice.group.service.GroupService;
import jparest.practice.user.domain.LoginType;
import jparest.practice.user.domain.User;
import jparest.practice.user.domain.UserType;
import jparest.practice.user.dto.SocialJoinRequest;
import jparest.practice.user.service.UserAuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.PayloadDocumentation;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;


import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class GroupControllerTest extends SpringContainerTest {

    private User firstUser;
    private Group group;
    
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
        firstUser = userAuthService.join(UserFixture.createFirstUser());

        authenticationSetup();

        CreateGroupResponse response = groupService.createGroup(firstUser, GroupFixture.groupName1);
        group = findGroupById(response.getId());
    }

    private void authenticationSetup() {
        String userId = String.valueOf(firstUser.getId());
        String accessToken = jwtService.createAccessToken(userId, UserType.ROLE_GENERAL.name());

        Authentication authentication = jwtService.tokenLogin(accessToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @Test
    @Transactional
    @DisplayName("그룹 리스트 조회")
    void getGroupList() throws Exception {

        //given

        //when
        mockMvc.perform(get("/api/groups")
//                .with(user(userId))
//                .header("Authorization", "Bearer " + accessToken)
//                .header("Cookie", "refreshToken=" + refreshToken)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andDo(document("get-groups",
                        PayloadDocumentation.responseFields(
                                PayloadDocumentation.fieldWithPath("success").description("성공 여부"),
                                PayloadDocumentation.fieldWithPath("result.[].groupId").description("그룹 아이디"),
                                PayloadDocumentation.fieldWithPath("result.[].groupName").description("그룹 이름"),
                                PayloadDocumentation.fieldWithPath("result.[].totalUsers").description("그룹 인원수")
                        )));

        //then
//        assertTrue(result);
    }

//    private ResultActions 유저_조회_요청(String accessToken) throws Exception {
//        return this.mockMvc.perform(get("/api/groups")
//                .contentType(MediaType.APPLICATION_JSON)
//                .header("Authorization", "Bearer " + accessToken));
//    }

    private void 유저_조회_요청(String accessToken) throws Exception {
        this.mockMvc.perform(get("/api/groups").contentType(MediaType.APPLICATION_JSON)).andDo(print());
//                .contentType(MediaType.APPLICATION_JSON)
//                .header("Authorization", "Bearer " + accessToken))
    }

    private Group findGroupById(Long groupId) {
        return groupRepository.findById(groupId).orElseThrow(() -> new GroupNotFoundException("groupId = " + groupId));
    }


}
