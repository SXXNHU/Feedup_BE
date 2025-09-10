package ll25.feedup.global.exception;

import org.springframework.http.HttpStatus;

public enum ExceptionCode {

    /** 공통 예외 **/
    BAD_REQUEST("COMMON.BAD_REQUEST", HttpStatus.BAD_REQUEST, "잘못된 요청입니다."),
    UNAUTHORIZED("COMMON.UNAUTHORIZED", HttpStatus.UNAUTHORIZED, "로그인이 필요합니다."),
    FORBIDDEN("COMMON.FORBIDDEN", HttpStatus.FORBIDDEN, "접근 권한이 없습니다."),
    NOT_FOUND("COMMON.NOT_FOUND", HttpStatus.NOT_FOUND, "대상을 찾을 수 없습니다."),
    CONFLICT("COMMON.CONFLICT", HttpStatus.CONFLICT, "요청 충돌이 발생했습니다."),
    UNSUPPORTED_MEDIA_TYPE("COMMON.UNSUPPORTED_MEDIA_TYPE", HttpStatus.UNSUPPORTED_MEDIA_TYPE, "지원하지 않는 컨텐츠 타입입니다."),
    INTERNAL_ERROR("COMMON.INTERNAL_ERROR", HttpStatus.INTERNAL_SERVER_ERROR, "서버 오류가 발생했습니다."),

    /** 도메인별 예외 **/
    HOST_NOT_FOUND("HOST.NOT_FOUND", HttpStatus.NOT_FOUND, "해당 호스트를 찾을 수 없습니다."),

    PLAN_NOT_FOUND("PLAN.NOT_FOUND", HttpStatus.NOT_FOUND, "해당 요금제를 찾을 수 없습니다."),

    PROMO_NOT_FOUND("PROMO.NOT_FOUND", HttpStatus.NOT_FOUND, "해당 프로모션을 찾을 수 없습니다."),
    PROMO_NOT_ACTIVE("PROMO.NOT_ACTIVE", HttpStatus.BAD_REQUEST, "모집 중인 프로모션이 아닙니다."),
    PROMO_FULL("PROMO.FULL", HttpStatus.BAD_REQUEST, "모집 정원이 가득 찼습니다."),

    APPLY_ALREADY("APPLY.ALREADY", HttpStatus.CONFLICT, "이미 신청했습니다."),

    REVIEW_INVALID_IMAGE("REVIEW.INVALID_IMAGE", HttpStatus.BAD_REQUEST, "허용되지 않은 이미지 경로가 포함되어 있습니다."),

    UPLOAD_FAILED("UPLOAD.FAILED", HttpStatus.BAD_REQUEST, "업로드 처리 중 오류가 발생했습니다.");

    // 1) status = 날려줄 상태코드
    // 2) code = 어느 부문의 오류인지 알려주는 카테고리 코드
    // 3) message = 발생한 예외에 대한 설명

    private final String code;
    private final HttpStatus status;
    private final String defaultMessage;

    ExceptionCode(String code, HttpStatus status, String defaultMessage) {
        this.code = code;
        this.status = status;
        this.defaultMessage = defaultMessage;
    }

    public String code() { return code; }
    public HttpStatus status() { return status; }
    public String defaultMessage() { return defaultMessage; }
}