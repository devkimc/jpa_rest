package jparest.practice.rest.exception;

import jparest.practice.common.error.BusinessException;
import jparest.practice.common.error.ErrorCode;

public class ExistGroupRestException extends BusinessException {
    public ExistGroupRestException(String message) {
        super(message, ErrorCode.EXIST_GROUP_REST);
    }
}
