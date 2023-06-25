package jparest.practice.group.exception;

import jparest.practice.common.error.BusinessException;
import jparest.practice.common.error.ErrorCode;

public class GroupUserNotFoundException extends BusinessException {
    public GroupUserNotFoundException(String message) {super(message, ErrorCode.GROUP_USER_NOT_FOUND);}
}
