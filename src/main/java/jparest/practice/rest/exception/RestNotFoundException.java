package jparest.practice.rest.exception;

import jparest.practice.common.error.BusinessException;
import jparest.practice.common.error.ErrorCode;

public class RestNotFoundException extends BusinessException {
    public RestNotFoundException(String message) {
        super(message, ErrorCode.REST_NOT_FOUND);
    }
}
