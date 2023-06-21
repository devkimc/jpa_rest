package jparest.practice.auth.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Slf4j
@Component
public class JwtTokenProvider implements InitializingBean {

    private final String AUTHORITIES_KEY = "auth";
    private final String secret;
    private final long accessTokenTTL;
    private final long refreshTokenTTL;
    private final String tokenIssure;
    private Key key;

    public JwtTokenProvider(@Value("${jwt.secret}") String secret,
                            @Value("${jwt.token.accessTokenTTL}") long accessTokenTTL,
                            @Value("${jwt.token.refreshTokenTTL}") long refreshTokenTTL,
                            @Value("${jwt.token.issure}") String tokenIssure)
    {
        this.secret = secret;
        this.accessTokenTTL = accessTokenTTL * 1000L;
        this.refreshTokenTTL = refreshTokenTTL * 1000L;
        this.tokenIssure = tokenIssure;
    }

    // accessToken 발급
    public String createAccessToken(String userId, String userType) {
        log.debug("accessTokenTTL : {}ms", accessTokenTTL);

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

    // refreshToken 발급
    public String createRefreshToken(String userId) {
        log.debug("Refresh : {}ms", refreshTokenTTL);

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

    public String getUserId(String jwt) {
        return (String) Jwts.parserBuilder()
                .setSigningKey(key)
                .build().parseClaimsJws(jwt)
                .getBody().get("userId");
    }

    public long getRefreshTokenTTL() {
        return refreshTokenTTL;
    }

    // 토큰 유효성 검증
    public boolean validateToken(String jwtToken) {
        try {
            Jws<Claims> claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build().parseClaimsJws(jwtToken);
            return !claims.getBody().getExpiration().before(new Date());
        } catch (SecurityException e) {
            log.error("잘못된 JWT 서명입니다.");
        } catch (ExpiredJwtException e) {
            log.debug("만료된 JWT 토큰입니다.");
        } catch (UnsupportedJwtException e) {
            log.error("지원되지 않는 JWT 토큰입니다.");
        } catch (IllegalArgumentException e) {
            log.error("JWT 토큰이 잘못되었습니다.");
        }
        return false;
    }

    // Bean 이 생성되고 의존성 주입까지 끝낸 후 주입 받은 secret 값을 key 변수에 할당하기 위함
    @Override
    public void afterPropertiesSet() throws Exception {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }
}
