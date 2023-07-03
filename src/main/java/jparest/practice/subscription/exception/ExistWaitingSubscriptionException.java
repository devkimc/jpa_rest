package jparest.practice.subscription.exception;

import jparest.practice.common.error.BusinessException;
import jparest.practice.common.error.ErrorCode;

public class ExistWaitingSubscriptionException extends BusinessException {

    public ExistWaitingSubscriptionException(String message) {
        super(message, ErrorCode.EXIST_WAITING_SUBSCRIPTION);
    }
}
