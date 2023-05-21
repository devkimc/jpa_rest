package jparest.practice.group.exception;

import jparest.practice.common.error.BusinessException;
import jparest.practice.common.error.ErrorCode;

public class GroupNotFoundException extends BusinessException {
    public GroupNotFoundException(String message) {super(message, ErrorCode.GROUP_NOT_FOUND);}
}
