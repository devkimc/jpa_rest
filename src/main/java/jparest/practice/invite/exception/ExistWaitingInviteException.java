package jparest.practice.invite.exception;

import jparest.practice.common.error.BusinessException;
import jparest.practice.common.error.ErrorCode;

public class ExistWaitingInviteException extends BusinessException {
    public ExistWaitingInviteException(String message) {
        super(message, ErrorCode.EXIST_WAITING_INVITE);
    }
}
