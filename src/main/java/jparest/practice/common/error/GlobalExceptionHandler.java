package jparest.practice.common.error;

import jparest.practice.user.exception.ExistLoginIdException;
import jparest.practice.user.exception.LoginFailException;
import jparest.practice.user.exception.UserNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // USER
    @ExceptionHandler(ExistLoginIdException.class)
    ResponseEntity<ErrorResponse> existUserHandler(ExistLoginIdException e) {
        log.error("ExistLoginIdException = {} {}", e.getErrorCode().getMessage(), e.getMessage());
        final ErrorResponse res = ErrorResponse.of(ErrorCode.EXIST_USER_ID);
        return new ResponseEntity<>(res, HttpStatus.valueOf(ErrorCode.EXIST_USER_ID.getStatus()));
    }

    @ExceptionHandler(LoginFailException.class)
    ResponseEntity<ErrorResponse> loginFailHandler(LoginFailException e) {
        log.error("loginFailHandler = {} {}", e.getErrorCode().getMessage(), e.getMessage());
        final ErrorResponse res = ErrorResponse.of(ErrorCode.LOGIN_FAIL);
        return new ResponseEntity<>(res, HttpStatus.valueOf(ErrorCode.LOGIN_FAIL.getStatus()));
    }

    @ExceptionHandler(UserNotFoundException.class)
    ResponseEntity<ErrorResponse> userNotFoundHandler(UserNotFoundException e) {
        log.error("userNotFoundHandler = {} {}", e.getErrorCode().getMessage(), e.getMessage());
        final ErrorResponse res = ErrorResponse.of(ErrorCode.USER_NOT_FOUND);
        return new ResponseEntity<>(res, HttpStatus.valueOf(ErrorCode.LOGIN_FAIL.getStatus()));
    }
}
