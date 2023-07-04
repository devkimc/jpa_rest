package jparest.practice.subscription.exception;

import jparest.practice.common.error.BusinessException;
import jparest.practice.common.error.ErrorCode;

public class AlreadyProcessedSubscriptionException extends BusinessException {
    public AlreadyProcessedSubscriptionException(String message) {
        super(message, ErrorCode.ALREADY_PROCESSED_SUBSCRIPTION);
    }
}
