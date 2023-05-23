package jparest.practice.common;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jparest.practice.auth.jwt.TokenType;
import jparest.practice.user.domain.UserType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.security.Key;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
public class JwtTokenProviderTest {

    private static final String AUTHORITIES_KEY = "auth";
    private final long accessTokenTTL = 3600;
    private final String tokenIssure = "jpa.rest.wskim";
    private final String secret = "spring0test0practice0spring0test0practice0secret";
    private Key key;

    @BeforeEach
    void setUp() {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    private final String joinUserId = "12312313123123";

    @Test
    @DisplayName("토큰 유효성 검증 성공")
    void success_validateToken() {
        //given
        String token = createAccessToken(joinUserId, UserType.ROLE_GENERAL.name());

        //when
        boolean result = validateToken(token);

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
