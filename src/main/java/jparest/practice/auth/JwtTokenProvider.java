package jparest.practice.auth;

import io.jsonwebtoken.*;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;

import static jparest.practice.auth.TokenType.*;

@Component
@Slf4j
public class JwtTokenProvider implements InitializingBean {

    private static final String AUTHORITIES_KEY = "auth";
    private final String secret;
    private final long accessTokenTTL;
    private final long refreshTokenTTL;
    private final String tokenIssure;
    private Key key;

    public JwtTokenProvider(@Value("${jwt.secret}") String secret,
                            @Value("${jwt.token.accessTokenTTL}") long accessTokenTTL,
                            @Value("${jwt.token.refreshTokenTTL}") long refreshTokenTTL,
                            @Value("${jwt.token.issure}") String tokenIssure
    ) {
        this.secret = secret;
        this.accessTokenTTL = accessTokenTTL;
        this.refreshTokenTTL = refreshTokenTTL;
        this.tokenIssure = tokenIssure;
    }

    public String createAccessToken(String userId, String userType) {
        log.debug("accessTokenTTL: {}ms", accessTokenTTL);

        Date now = new Date();
        return Jwts.builder()
                .setSubject(ACCESS_TOKEN.name())
                .signWith(key, SignatureAlgorithm.HS256)
                .claim(AUTHORITIES_KEY, userType)
                .claim("userId", userId)
                .setIssuer(tokenIssure)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + this.accessTokenTTL))
                .compact();
    }

    public String createRefreshToken(String userId) {
        log.debug("RefreshTokenTTL : {}ms", refreshTokenTTL);

        Date now = new Date();
        return Jwts.builder()
                .setSubject(REFRESH_TOKEN.name())
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
                .build().parseClaimsJws(jwt).getBody().get("userId");
    }

    public long getRefreshTokenTTL() {
        return refreshTokenTTL;
    }

    public boolean validateToken(String jwtToken) {
        try {
            Jws<Claims> claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build().parseClaimsJws(jwtToken);
            return !claims.getBody().getExpiration().before(new Date());
        } catch (SecurityException e) {
            log.error("Invalid JWT signature");
        } catch (MalformedJwtException e) {
            log.error("Invalid JWT token");
        } catch (ExpiredJwtException e) {
            log.error("Expired JWT token");
        } catch (UnsupportedJwtException e) {
            log.error("Unsupported JWT token.");
        } catch (IllegalArgumentException e) {
            log.error("JWT token compact of handler are invalid");
        }
        return false;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

}
