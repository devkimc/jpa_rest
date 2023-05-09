package jparest.practice.user.exception;

import jparest.practice.common.error.BusinessException;
import jparest.practice.common.error.ErrorCode;

public class UserNotFoundException extends BusinessException {
    public UserNotFoundException(String id) {
        super(id, ErrorCode.LOGIN_FAIL);
    }
}
