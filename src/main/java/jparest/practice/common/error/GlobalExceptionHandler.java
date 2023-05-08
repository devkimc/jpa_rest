package jparest.practice.common.error;

import jparest.practice.member.exception.ExistLoginIdException;
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
}
