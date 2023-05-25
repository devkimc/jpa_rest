package jparest.practice.controller;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jparest.practice.auth.jwt.JwtService;
import jparest.practice.auth.jwt.JwtTokenProvider;
import jparest.practice.auth.jwt.TokenType;
import jparest.practice.group.domain.Group;
import jparest.practice.group.domain.UserGroup;
import jparest.practice.group.service.GroupService;
import jparest.practice.invite.controller.InviteController;
import jparest.practice.invite.domain.Invite;
import jparest.practice.invite.domain.InviteStatus;
import jparest.practice.invite.repository.InviteRepository;
import jparest.practice.invite.service.InviteService;
import jparest.practice.user.domain.LoginType;
import jparest.practice.user.domain.User;
import jparest.practice.user.domain.UserType;
import jparest.practice.user.dto.SocialJoinRequest;
import jparest.practice.user.service.UserAuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.security.Key;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@WebMvcTest(InviteController.class)
public class InviteControllerTest {

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
    UserAuthService userAuthService;

    @MockBean
    InviteService inviteService;

    @MockBean
    InviteRepository inviteRepository;

    @MockBean
    GroupService groupService;

    @MockBean
    JwtService jwtService;

    @MockBean
    JwtTokenProvider jwtTokenProvider;

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
    private final String tokenIssure = "jpa.rest.wskim";
    private final String secret = "spring0test0practice0spring0test0practice0secret";
    private Key key;

    @BeforeEach
    void setUp() {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        this.key = Keys.hmacShaKeyFor(keyBytes);

        System.out.println("key = " + key);
        joinUser1 = joinSetup(new SocialJoinRequest(socialUserId1, email1, nickname1, loginType1));
        joinUser2 = joinSetup(new SocialJoinRequest(socialUserId2, email2, nickname2, loginType2));
        System.out.println("joinUser1 = " + joinUser1);

//        group1 = groupService.createGroup(joinUser1, groupName);
//        System.out.println("group1 = " + group1);

    }

    private User joinSetup(SocialJoinRequest socialJoinRequest) {
        return userAuthService.join(socialJoinRequest);
    }

    private final String joinUserId = "12312313123123";

    @Test
    @DisplayName("토큰 유효성 검증 성공")
    void success_validateToken() {
        //given
        String token = createAccessToken(joinUserId, UserType.ROLE_GENERAL.name());

        //when
        boolean result = validateToken(token);
        System.out.println("joinUser1 = " + joinUser1);

        //then
        assertTrue(result);
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
