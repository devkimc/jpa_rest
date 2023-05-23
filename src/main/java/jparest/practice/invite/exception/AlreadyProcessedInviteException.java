package jparest.practice.invite.exception;

import jparest.practice.common.error.BusinessException;
import jparest.practice.common.error.ErrorCode;

public class AlreadyProcessedInviteException extends BusinessException {
    public AlreadyProcessedInviteException(String message) {
        super(message, ErrorCode.ALREADY_PROCESSED_INVITE);
    }
}
