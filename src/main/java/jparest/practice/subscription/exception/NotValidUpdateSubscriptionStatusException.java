package jparest.practice.subscription.exception;

import jparest.practice.common.error.BusinessException;
import jparest.practice.common.error.ErrorCode;

public class NotValidUpdateSubscriptionStatusException extends BusinessException {
    public NotValidUpdateSubscriptionStatusException(String message) {
        super(message, ErrorCode.NOT_VALID_SUBSCRIPTION_INVITE_STATUS);
    }
}
