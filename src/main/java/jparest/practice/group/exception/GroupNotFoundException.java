package jparest.practice.group.exception;

import jparest.practice.common.error.BusinessException;
import jparest.practice.common.error.ErrorCode;

public class GroupNotFoundException extends BusinessException {
    public GroupNotFoundException(String id) {super(id, ErrorCode.GROUP_NOT_FOUND);}
}
