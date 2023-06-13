package jparest.practice.common.error;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {
    private String message;
    private int status;
    private String code;
    private String field;

    private ErrorResponse(final ErrorCode code) {
        this.message = code.getMessage();
        this.status = code.getStatus();
        this.code = code.getCode();
    }

    // Api 요청 필드 에러 시
    private ErrorResponse(final ErrorCode code, final String message, final String field) {
        this.message = message;
        this.status = code.getStatus();
        this.code = code.getCode();
        this.field = field;
    }

    public static ErrorResponse of(final ErrorCode code) {
        return new ErrorResponse(code);
    }

    public static ErrorResponse of(final ErrorCode code, final String message, final String field) {
        return new ErrorResponse(code, message, field);
    }

}
