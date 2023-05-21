package jparest.practice.group.exception;

import jparest.practice.common.error.BusinessException;
import jparest.practice.common.error.ErrorCode;

public class ExistUserGroupException extends BusinessException {
    public ExistUserGroupException(String message) {
        super(message, ErrorCode.EXIST_USER_GROUP);
    }
}
