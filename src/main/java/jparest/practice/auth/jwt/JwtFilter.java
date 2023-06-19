package jparest.practice.auth.jwt;

import jparest.practice.common.util.CookieUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.stream.Collectors;


@Slf4j
@RequiredArgsConstructor
@Component
public class JwtFilter extends OncePerRequestFilter {
    @Value("${domain.host}")
    private String domain;

    private final JwtService jwtService;

    // JWT 토큰 검사
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        log.info("JWT Filter 실행 시작");

        boolean isValidationAccessToken = false;
        boolean isValidationRefreshToken = false;

        final Cookie accessTokenCookie = CookieUtils.getCookie(request, TokenType.ACCESS_TOKEN.name()).orElse(null);
        final Cookie refreshTokenCookie = CookieUtils.getCookie(request, TokenType.REFRESH_TOKEN.name()).orElse(null);

        String accessToken = null;
        String refreshToken = null;

        if (accessTokenCookie != null) {
            accessToken = CookieUtils.deserialize(accessTokenCookie, String.class);
            isValidationAccessToken = jwtService.validateToken(accessToken);
        }

        if (refreshTokenCookie != null) {
            refreshToken = CookieUtils.deserialize(refreshTokenCookie, String.class);
            isValidationRefreshToken = jwtService.validateToken(refreshToken) && jwtService.existedByRefreshToken(refreshToken);
        }

        log.info("액세스 토큰 유효성 체크 결과 ({})", isValidationAccessToken);
        log.info("리프레시 토큰 유효성 체크 결과 ({})", isValidationRefreshToken);

        // 리프레시 토큰이 검증되지 않았을 경우 쿠키에서 전부 삭제
        if (isValidationRefreshToken == false) {
            CookieUtils.deleteCookie(request, response, TokenType.REFRESH_TOKEN.name());
            CookieUtils.deleteCookie(request, response, TokenType.ACCESS_TOKEN.name());

            if (isValidationAccessToken == true) {
                jwtService.deleteRefreshTokenByAccessToken(accessToken);
            }
        }

        // 액세스 X, 리프레시 O
        if (!isValidationAccessToken && isValidationRefreshToken) {
            setAuthentication(refreshToken, request, response);
        }

        // 액세스 O, 리프레시 O
        if (isValidationAccessToken && isValidationRefreshToken) {
            setAuthentication(accessToken, request, response);
        }

        log.info("JWT Filter 실행 종료");
        filterChain.doFilter(request, response);
    }

    // 존재하는 유저인지 확인 후, UsernamePasswordAuthenticationToken 을 SecurityContext 에 추가한다.
    private void setAuthentication(String token, HttpServletRequest request, HttpServletResponse response) {
        try {
            Authentication authentication = jwtService.tokenLogin(token);
            String userType = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.joining(","));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            CookieUtils.addCookie(response, TokenType.ACCESS_TOKEN.name(), jwtService.createAccessToken(authentication.getName(), userType), domain);
        } catch (UsernameNotFoundException e) {
            log.error("UsernameNotFoundException : 회원이 존재하지 않습니다. DB 확인해주세요.");
            CookieUtils.deleteCookie(request, response, TokenType.REFRESH_TOKEN.name());
            CookieUtils.deleteCookie(request, response, TokenType.ACCESS_TOKEN.name());
        }
    }

    // WhiteList 에 존재하는 URL 은 필터 검사를 제외시킨다.
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {

        for(String url: JwtFilterWhiteList.WHITELIST) {
                if(request.getRequestURI().equalsIgnoreCase(url)) {
                    return true;
                }
            }
        return false;
    }
}
