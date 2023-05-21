package jparest.practice.invite.exception;

import jparest.practice.common.error.BusinessException;
import jparest.practice.common.error.ErrorCode;

public class ExistInviteForUserException extends BusinessException {
    public ExistInviteForUserException(String message) {
        super(message, ErrorCode.EXIST_INVITE_FOR_USER);
    }
}
