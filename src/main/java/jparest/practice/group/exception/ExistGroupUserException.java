package jparest.practice.group.exception;

import jparest.practice.common.error.BusinessException;
import jparest.practice.common.error.ErrorCode;

public class ExistGroupUserException extends BusinessException {
    public ExistGroupUserException(String message) {
        super(message, ErrorCode.EXIST_GROUP_USER);
    }
}
