package jparest.practice.common.error;

import jparest.practice.group.exception.GroupNotFoundException;
import jparest.practice.user.exception.ExistLoginIdException;
import jparest.practice.user.exception.LoginFailException;
import jparest.practice.user.exception.UserNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Optional;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // USER
    @ExceptionHandler(ExistLoginIdException.class)
    ResponseEntity<ErrorResponse> existUserHandler(ExistLoginIdException e) {
        return getErrorResponseEntity(e, ErrorCode.LOGIN_FAIL);
    }

    @ExceptionHandler(LoginFailException.class)
    ResponseEntity<ErrorResponse> loginFailHandler(LoginFailException e) {
        return getErrorResponseEntity(e, ErrorCode.LOGIN_FAIL);
    }

    @ExceptionHandler(UserNotFoundException.class)
    ResponseEntity<ErrorResponse> userNotFoundHandler(UserNotFoundException e) {
        return getErrorResponseEntity(e, ErrorCode.USER_NOT_FOUND);
    }

    // GROUP
    @ExceptionHandler(GroupNotFoundException.class)
    ResponseEntity<ErrorResponse> groupNotFoundHandler(GroupNotFoundException e) {
        return getErrorResponseEntity(e, ErrorCode.GROUP_NOT_FOUND);
    }

    private static ResponseEntity<ErrorResponse> getErrorResponseEntity(BusinessException e, ErrorCode errorCode) {
        log.error("GlobalException = {} {}", e.getErrorCode().getMessage(), e.getMessage());

        final ErrorResponse res = ErrorResponse.of(errorCode);
        return new ResponseEntity<>(res, HttpStatus.valueOf(errorCode.getStatus()));
    }
}
