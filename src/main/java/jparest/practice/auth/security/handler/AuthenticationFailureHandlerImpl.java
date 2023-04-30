package jparest.practice.auth.security.handler;

import jparest.practice.common.ApiResponse;
import jparest.practice.common.ApiResponseType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class AuthenticationFailureHandlerImpl implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException {
        System.out.println("AuthenticationFailureHandlerImpl.onAuthenticationFailure + UNAUTHORIZED_RESPONSE");
        ApiResponse.error(response, ApiResponseType.UNAUTHORIZED_RESPONSE);
    }
}
