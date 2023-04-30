package jparest.practice.auth.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jparest.practice.auth.security.dto.LoginDTO;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Log4j2
public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    public CustomAuthenticationFilter(AuthenticationManager authenticationManager) {
        super.setAuthenticationManager(authenticationManager);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        final UsernamePasswordAuthenticationToken authRequest;

        final LoginDTO loginDTO;
        try {
            // 사용자 요청 정보로 UserPasswordAuthentication 발급
            loginDTO = new ObjectMapper().readValue(request.getInputStream(), LoginDTO.class);
            authRequest = new UsernamePasswordAuthenticationToken(loginDTO.getLoginId(), loginDTO.getPassword());
            System.out.println("UsernamePasswordAuthenticationToken 토큰 생성" + authRequest);
        } catch (IOException e) {
            throw new RuntimeException("Token 발급 실패");
        }
        setDetails(request, authRequest);

        // AuthenticationManager 에게 전달 -> AuthenticationProvider 의 인증 메서드 실행
        System.out.println("AuthenticationManager 에게 전달 -> AuthenticationProvider");
        return this.getAuthenticationManager().authenticate(authRequest);
    }
}
