package jparest.practice.group.exception;

import jparest.practice.common.error.BusinessException;
import jparest.practice.common.error.ErrorCode;

public class UserGroupNotFoundException extends BusinessException {
    public UserGroupNotFoundException(String message) {super(message, ErrorCode.USER_GROUP_NOT_FOUND);}
}
