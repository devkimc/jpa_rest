package jparest.practice.invite.exception;

import jparest.practice.common.error.BusinessException;
import jparest.practice.common.error.ErrorCode;

public class InviteNotFoundException extends BusinessException {
    public InviteNotFoundException(String message) {
        super(message, ErrorCode.INVITE_NOT_FOUND);
    }
}
