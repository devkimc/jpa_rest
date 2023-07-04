package jparest.practice.subscription.exception;

import jparest.practice.common.error.BusinessException;
import jparest.practice.common.error.ErrorCode;

public class SubscriptionNotFoundException extends BusinessException {
    public SubscriptionNotFoundException(String message) {
        super(message, ErrorCode.SUBSCRIPTION_NOT_FOUND);
    }
}
