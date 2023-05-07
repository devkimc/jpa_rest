package jparest.practice.auth.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import jparest.practice.common.error.ErrorResponse;
import jparest.practice.common.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

@Component
@RequiredArgsConstructor
public class AuthDeniedHandler implements AccessDeniedHandler {
    private final ObjectMapper objectMapper;

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        final ErrorResponse errorResponse = ErrorResponse.of(ErrorCode.ACCESS_DENIED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(errorResponse.getStatus());
        try (OutputStream os = response.getOutputStream()) {
            objectMapper.writeValue(os, errorResponse);
            os.flush();
        }
    }
}
