package jparest.practice.auth.config;

import jparest.practice.auth.jwt.JwtFilter;
import jparest.practice.auth.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.CorsUtils;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.lang.reflect.Array;
import java.util.Arrays;

@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig  {
    private final CorsConfig corsProperties;
    private final JwtFilter jwtFilter;

    // JWT 제공 클래스
    private final JwtTokenProvider jwtProvider;
    // 인증 실패 또는 인증헤더가 전달받지 못했을 때 핸들러
    private final AuthenticationEntryPoint authenticationEntryPoint;
    // 인가 실패 핸들러
    private final AccessDeniedHandler accessDeniedHandler;

    /**
     * 보안 기능 초기화 및 설정
     * WebSecurityConfigureAdapter Deprecated 로 인한 수정
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        final String[] POST_WHITELIST = new String[]{
                "/login",
                "/user"
        };

        return http
                .headers().frameOptions().sameOrigin()
                .and()
                .formLogin().disable()
                .cors().and()
                .csrf().disable()
                .httpBasic().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .requestMatchers(CorsUtils::isPreFlightRequest).permitAll()
                .antMatchers(HttpMethod.POST, POST_WHITELIST).permitAll() // 해당 POST URL은 모두 허용
                .antMatchers("**").hasAnyRole("USER") // 권한 적용
                .anyRequest().authenticated() // 나머지 요청에 대해서는 인증을 요구
                .and()
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling()
                .authenticationEntryPoint(authenticationEntryPoint) // 인증 실패
                .accessDeniedHandler(accessDeniedHandler) // 인가 실패
                .and()
                .build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        UrlBasedCorsConfigurationSource corsConfigSource = new UrlBasedCorsConfigurationSource();

        CorsConfiguration corsConfig = new CorsConfiguration();
        corsConfig.setAllowedHeaders(Arrays.asList(corsProperties.getAllowedHeaders().split(",")));
        corsConfig.setAllowedMethods(Arrays.asList(corsProperties.getAllowedMethods().split(",")));
        corsConfig.setAllowedOrigins(Arrays.asList(corsProperties.getAllowedOrigins().split(",")));
        corsConfig.setAllowCredentials(true);
        corsConfig.setMaxAge(corsConfig.getMaxAge());

        corsConfigSource.registerCorsConfiguration("/**", corsConfig);
        return corsConfigSource;
    }

    /**
     * 비밀번호 암호화 및 확인 클래스
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
