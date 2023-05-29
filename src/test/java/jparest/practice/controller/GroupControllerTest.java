package jparest.practice.controller;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jparest.SpringContainerTest;
import jparest.practice.auth.jwt.JwtFilter;
import jparest.practice.auth.jwt.JwtService;
import jparest.practice.auth.jwt.JwtTokenProvider;
import jparest.practice.auth.jwt.TokenType;
import jparest.practice.auth.security.CustomUserDetailService;
import jparest.practice.common.util.ApiResult;
import jparest.practice.group.controller.GroupController;
import jparest.practice.group.domain.Group;
import jparest.practice.group.domain.UserGroup;
import jparest.practice.group.service.GroupService;
import jparest.practice.invite.domain.Invite;
import jparest.practice.invite.domain.InviteStatus;
import jparest.practice.invite.service.InviteService;
import jparest.practice.user.controller.UserController;
import jparest.practice.user.domain.LoginType;
import jparest.practice.user.domain.User;
import jparest.practice.user.domain.UserType;
import jparest.practice.user.dto.SocialJoinRequest;
import jparest.practice.user.dto.SocialLoginResponse;
import jparest.practice.user.service.UserAuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
//import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
//import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;


import java.security.Key;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

public class GroupControllerTest extends SpringContainerTest {

    private final String socialUserId1 = "123123";
    private final String email1 = "eee@www.aaaa";
    private final String nickname1 = "유저1";
    private final LoginType loginType1 = LoginType.KAKAO;

    private final String socialUserId2 = "234234";
    private final String email2 = "eee@www.bbb";
    private final String nickname2 = "유저2";
    private final LoginType loginType2 = LoginType.KAKAO;

    private final String groupName = "유저 1의 나라";

    @Autowired
    UserController userController;

    @Autowired
    JwtService jwtService;

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @Autowired
    JwtFilter jwtFilter;

    @Autowired
    UserAuthService userAuthService;

    @Autowired
    GroupService groupService;

    @Autowired
    CustomUserDetailService userDetailService;

    @Autowired
    InviteService inviteService;

    private UserDetails userDetails;

    User joinUser1;
    User joinUser2;

    Group group1;

    @Test
    public void 그룹초대() throws Exception {

        //given
        List<UserGroup> userGroups = joinUser1.getUserGroups();

        //when
        Invite invite = inviteService.inviteToGroup(group1.getId(), joinUser1, joinUser2.getId());

        //then
        assertAll(
                () -> assertEquals(InviteStatus.WAITING, invite.getInviteStatus()), // 초대 상태
                () -> assertEquals(userGroups.get(0), invite.getSendUserGroup()), // 초대한 유저
                () -> assertEquals(joinUser2, invite.getRecvUser()) // 초대받은 유저
        );
    }

    private static final String AUTHORITIES_KEY = "auth";
    private final long accessTokenTTL = 3600;
    private final long refreshTokenTTL = 3600000;
    private final String tokenIssure = "jpa.rest.wskim";
    private final String secret = "spring0test0practice0spring0test0practice0secret";
    private Key key;

    @BeforeEach
    void setUp() {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        this.key = Keys.hmacShaKeyFor(keyBytes);

        joinUser1 = joinSetup(new SocialJoinRequest(socialUserId1, email1, nickname1, loginType1));
        joinUser2 = joinSetup(new SocialJoinRequest(socialUserId2, email2, nickname2, loginType2));

        group1 = groupService.createGroup(joinUser1, groupName);
        userDetails = userDetailService.loadUserByUsername(joinUser1.getId().toString());

        System.out.println("joinUser1 = " + joinUser1);
        System.out.println("group1 = " + group1);

    }

    private User joinSetup(SocialJoinRequest socialJoinRequest) {
        return userAuthService.join(socialJoinRequest);
    }

    private final String joinUserId = "12312313123123";

    @Test
    @DisplayName("그룹 리스트 조회")
    void getGroupList() throws Exception {
        //given
        String accessToken = createAccessToken(joinUserId, UserType.ROLE_GENERAL.name());
        String refreshToken = createRefreshToken(joinUserId);

        //when
//        ResultActions resultActions = 유저_조회_요청(accessToken);
//        System.out.println("resultActions = " + resultActions);
        RequestPostProcessor requestPostProcessor = user(userDetails);


//        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        System.out.println("principal = " + principal);
//        System.out.println("authentication = " + authentication);

        mockMvc.perform(get("/api/groups")
                .with(user(userDetails))
                .header("Authorization", "Bearer " + accessToken)
                .header("Cookie", "refreshToken=" + refreshToken)
                .contentType(MediaType.APPLICATION_JSON)
        ).andDo(print());

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

    public String createAccessToken(String userId, String userType) {

        Date now = new Date();
        return Jwts.builder()
                .setSubject(TokenType.ACCESS_TOKEN.name())
                .signWith(key, SignatureAlgorithm.HS256)
                .claim(AUTHORITIES_KEY, userType)
                .claim("userId", userId)
                .setIssuer(tokenIssure)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + this.accessTokenTTL))
                .compact();
    }

    /**
     * refreshToken 발급
     */
    public String createRefreshToken(String userId) {

        Date now = new Date();
        return Jwts.builder()
                .setSubject(TokenType.REFRESH_TOKEN.name())
                .signWith(key, SignatureAlgorithm.HS256)
                .claim("userId", userId)
                .setIssuer(tokenIssure)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + this.refreshTokenTTL))
                .compact();
    }

    public boolean validateToken(String jwtToken) {
        try {
            Jws<Claims> claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build().parseClaimsJws(jwtToken);
            return !claims.getBody().getExpiration().before(new Date());
        } catch (Exception e) {
            System.out.println("e = " + e);
        }

        return false;
    }
}
