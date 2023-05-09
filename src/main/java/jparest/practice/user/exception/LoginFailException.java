package jparest.practice.user.exception;

import jparest.practice.common.error.BusinessException;
import jparest.practice.common.error.ErrorCode;

public class LoginFailException extends BusinessException {
    public LoginFailException(String loginId) {
        super(loginId, ErrorCode.LOGIN_FAIL);
    }
}
