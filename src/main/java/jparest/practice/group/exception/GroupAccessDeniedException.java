package jparest.practice.group.exception;

import jparest.practice.common.error.BusinessException;
import jparest.practice.common.error.ErrorCode;

public class GroupAccessDeniedException extends BusinessException {
    public GroupAccessDeniedException(String message) {
        super(message, ErrorCode.GROUP_ACCESS_DENIED);
    }
}
