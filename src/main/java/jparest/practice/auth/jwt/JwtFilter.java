package jparest.practice.auth.jwt;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String BEARER_PREFIX = "Bearer";
    private final JwtProvider jwtProvider;
    private final List<String> EXCLUDE_URL = List.of("/user", "/login");

    /**
     * 토큰 인증 정보를 현재 쓰레드의 SecurityContext 에 저장되는 역할 수행
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // Request Header 에서 토큰 추출
        String jwt = resolveToken(request);
        System.out.println("jwt = " + jwt);

        // Token 유효성 검사
        if (StringUtils.hasText(jwt) && jwtProvider.isValidToken(jwt)) {
            System.out.println("jwt 통과");

            // 토큰으로 인증 정보를 추출
            Authentication authentication = jwtProvider.getAuthentication(jwt);
            // SecurityContext 에 저장
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } else {
            System.out.println("Jwt 실패");
        }

        filterChain.doFilter(request, response);
    }

    /**
     * Request Header 에서 토큰 추출
     */
    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
