package jparest.practice.rest.exception;

import jparest.practice.common.error.BusinessException;
import jparest.practice.common.error.ErrorCode;

public class GroupRestNotFoundException extends BusinessException {
    public GroupRestNotFoundException(String message) {
        super(message, ErrorCode.GROUP_REST_NOT_FOUND);
    }
}
