package ll25.feedup.global.exception;

import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {

    private final ExceptionCode exceptionCode;

    public BusinessException(ExceptionCode code) {
        super(code.defaultMessage());
        this.exceptionCode = code;
    }

    public BusinessException(ExceptionCode code, String message) {
        super(message);
        this.exceptionCode = code;
    }

    public BusinessException(ExceptionCode code, String message, Throwable cause) {
        super(message, cause);
        this.exceptionCode = code;
    }
}