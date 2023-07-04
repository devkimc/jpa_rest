package jparest.practice.invite.exception;

import jparest.practice.common.error.BusinessException;
import jparest.practice.common.error.ErrorCode;

public class NotValidUpdateInviteStatusException extends BusinessException {
    public NotValidUpdateInviteStatusException(String message) {
        super(message, ErrorCode.NOT_VALID_UPDATE_INVITE_STATUS);
    }
}
