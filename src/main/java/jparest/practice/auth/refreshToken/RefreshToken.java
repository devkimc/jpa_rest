package jparest.practice.auth.refreshToken;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

import javax.persistence.Id;
import java.util.concurrent.TimeUnit;

@Getter
@RedisHash("Token")
@NoArgsConstructor
public class RefreshToken {
    @Id
    private String id;

    private String refreshToken;

    @TimeToLive(unit = TimeUnit.MILLISECONDS)
    private long refreshTokenTTL;

    public static RefreshToken create(String id, String refreshToken, long refreshTokenTTL) {
        return new RefreshToken(id, refreshToken, refreshTokenTTL);
    }

    public RefreshToken(String id, String refreshToken, long refreshTokenTTL) {
        this.id = id;
        this.refreshToken = refreshToken;
        this.refreshTokenTTL = refreshTokenTTL;
    }
}
