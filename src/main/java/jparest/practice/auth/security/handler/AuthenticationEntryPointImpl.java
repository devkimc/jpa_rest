package jparest.practice.auth.security.handler;

import jparest.practice.common.ApiResponse;
import jparest.practice.common.ApiResponseType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class AuthenticationEntryPointImpl implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authenticationException) throws IOException, ServletException {
        System.out.println("request = " + request);
        System.out.println("authenticationException = " + authenticationException);

        ApiResponse.error(response, ApiResponseType.UNAUTHORIZED_RESPONSE);
    }
}
