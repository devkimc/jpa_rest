package jparest.practice.common.error;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum ErrorCode {

    // COMMON
    AUTHENTICATION_ENTRYPOINT(401, "C01", "로그인 후 사용 가능합니다."),
    ACCESS_DENIED(403, "C03", "권한이 없습니다."),

    // USER
    EXIST_USER_ID(400, "U01", "이미 존재하는 아이디입니다."),
    LOGIN_FAIL(400, "U02", "로그인에 실패했습니다."),
    USER_NOT_FOUND(404, "U03", "존재하지 않는 유저입니다."),

    // GROUP
    GROUP_NOT_FOUND(404, "G01", "존재하지 않는 그룹입니다."),
    USER_GROUP_NOT_FOUND(404, "G02", "해당 유저는 그룹에 존재하지 않습니다."),
    EXIST_USER_GROUP(400, "G03", "이미 그룹에 존재하는 유저입니다."),

    // INVITE
    EXIST_INVITE_FOR_USER(400, "I01", "이미 초대한 유저입니다."),
    INVITE_NOT_FOUND(404, "I02", "존재하지 않는 초대입니다"),
    ALREADY_PROCESSED_INVITE(400, "I03", "이미 처리된 초대입니다."),

    // REST
    REST_NOT_FOUND(404, "R01", "존재하지 않는 식당입니다."),
    GROUP_REST_NOT_FOUND(404, "R02", "존재하지 않는 그룹식당입니다."),
    EXIST_GROUP_REST(400, "R03", "존재하는 그룹식당입니다."),
    ;


    private final String code;
    private final String message;
    private final int status;

    ErrorCode(final int status, final String code, final String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public int getStatus() {
        return status;
    }
}
