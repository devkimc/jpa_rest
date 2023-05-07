package jparest.practice.member.repository;

import jparest.practice.common.error.BusinessException;
import jparest.practice.common.error.ErrorCode;

public class ExistLoginIdException extends BusinessException {
    public ExistLoginIdException(String nickname) {
        super(nickname, ErrorCode.EXIST_USER_ID);
    }
}
