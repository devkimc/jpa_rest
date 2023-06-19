package jparest.practice.auth.jwt;

import jparest.practice.auth.security.CustomUserDetailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class JwtService {
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenRepository tokenRepository;
    private final CustomUserDetailService userDetailService;

    public String createAccessToken(String userId, String userType) {
        return jwtTokenProvider.createAccessToken(userId, userType);
    }

    @Transactional
    public String createRefreshToken(String userId) {
        String refreshToken = jwtTokenProvider.createRefreshToken(userId);
        RefreshToken token = RefreshToken.create(userId, refreshToken, jwtTokenProvider.getRefreshTokenTTL());

        return tokenRepository.save(token).getRefreshToken();
    }

    public boolean validateToken(String token) {
        return jwtTokenProvider.validateToken(token);
    }

    public boolean existedByRefreshToken(String refreshToken) {
        String userId = jwtTokenProvider.getUserId(refreshToken);
        RefreshToken token = tokenRepository.findById(userId).orElse(null);
        return token != null && token.getRefreshToken().equals(refreshToken);
    }

    @Transactional
    public void deleteRefreshTokenByAccessToken(String accessToken) {
        tokenRepository.deleteById(jwtTokenProvider.getUserId(accessToken));
    }

    @Transactional
    public void deleteRefreshTokenByUserId(String userId) {
        tokenRepository.deleteById(userId);
    }

    public Authentication tokenLogin(String jwt) {
        UserDetails userDetails = userDetailService.loadUserByUsername(jwtTokenProvider.getUserId(jwt));
        return new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities()
        );
    }
}
