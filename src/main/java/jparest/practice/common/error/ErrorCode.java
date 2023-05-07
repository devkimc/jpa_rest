package jparest.practice.common.error;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum ErrorCode {

    // COMMON
    AUTHENTICATION_ENTRYPOINT(401, "C01", "로그인 후 사용 가능합니다."),
    ACCESS_DENIED(403, "C03", "권한이 없습니다."),
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
