package jparest.practice.subscription.exception;

import jparest.practice.common.error.BusinessException;
import jparest.practice.common.error.ErrorCode;

public class ExistSubscriptionException extends BusinessException {

    public ExistSubscriptionException(String message) {
        super(message, ErrorCode.EXIST_WAITING_SUBSCRIPTION);
    }
}
