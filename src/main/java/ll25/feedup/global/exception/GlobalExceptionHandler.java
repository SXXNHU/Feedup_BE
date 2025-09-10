package ll25.feedup.global.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.util.StringUtils;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartException;

import java.net.URI;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    /** 1) 비즈니스 예외 **/
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ProblemDetail> handleBusiness(BusinessException ex, HttpServletRequest req) {
        ExceptionCode ec = ex.getExceptionCode();
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(ec.status(), message(ex));
        decorate(pd, ec, req, null);
        return ResponseEntity.status(ec.status()).body(pd);
    }

    /** 2) 검증 예외 **/
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ProblemDetail> handleInvalid(MethodArgumentNotValidException ex, HttpServletRequest req) {
        List<Map<String, Object>> errors = ex.getBindingResult().getFieldErrors().stream()
                .map(this::toMap)
                .toList();
        ExceptionCode ec = ExceptionCode.BAD_REQUEST;
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(ec.status(), "요청 값이 유효하지 않습니다.");
        decorate(pd, ec, req, Map.of("errors", errors));
        return ResponseEntity.badRequest().body(pd);
    }

    /** 3) 파라미터/본문/미디어타입/메서드 에러 **/
    @ExceptionHandler({
            MissingServletRequestParameterException.class,
            HttpMessageNotReadableException.class
    })
    public ResponseEntity<ProblemDetail> handleBadReq(Exception ex, HttpServletRequest req) {
        ExceptionCode ec = ExceptionCode.BAD_REQUEST;
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(ec.status(), friendlyMessage(ex));
        decorate(pd, ec, req, null);
        return ResponseEntity.badRequest().body(pd);
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<ProblemDetail> handleMedia(HttpServletRequest req) {
        ExceptionCode ec = ExceptionCode.UNSUPPORTED_MEDIA_TYPE;
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(ec.status(), ec.defaultMessage());
        decorate(pd, ec, req, null);
        return ResponseEntity.status(ec.status()).body(pd);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ProblemDetail> handleMethod(HttpServletRequest req) {
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(HttpStatus.METHOD_NOT_ALLOWED, "지원하지 않는 HTTP 메서드입니다.");
        pd.setInstance(URI.create(req.getRequestURI()));
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(pd);
    }

    /** 4) 파일 업로드 **/
    @ExceptionHandler({MaxUploadSizeExceededException.class, MultipartException.class})
    public ResponseEntity<ProblemDetail> handleUpload(HttpServletRequest req) {
        ExceptionCode ec = ExceptionCode.UPLOAD_FAILED;
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(ec.status(), ec.defaultMessage());
        decorate(pd, ec, req, null);
        return ResponseEntity.badRequest().body(pd);
    }

    /** 5) 보안 **/
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ProblemDetail> handleAccessDenied(HttpServletRequest req) {
        ExceptionCode ec = ExceptionCode.FORBIDDEN;
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(ec.status(), ec.defaultMessage());
        decorate(pd, ec, req, null);
        return ResponseEntity.status(ec.status()).body(pd);
    }

    /** 6) 그 외 **/
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ProblemDetail> handleEtc(Exception ex, HttpServletRequest req) {
        ExceptionCode ec = ExceptionCode.INTERNAL_ERROR;
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(ec.status(), ec.defaultMessage());
        decorate(pd, ec, req, Map.of("exception", ex.getClass().getName()));
        return ResponseEntity.status(ec.status()).body(pd);
    }

    private Map<String, Object> toMap(FieldError fe) {
        return Map.of("field", fe.getField(), "rejectedValue", fe.getRejectedValue(), "reason", fe.getDefaultMessage());
    }

    private void decorate(ProblemDetail pd, ExceptionCode ec, HttpServletRequest req, Map<String, ?> extraProps) {
        pd.setTitle(ec.code()); // 비즈니스 코드
        pd.setInstance(URI.create(req.getRequestURI()));
        pd.setProperty("timestamp", OffsetDateTime.now().toString());
        if (extraProps != null) extraProps.forEach(pd::setProperty);
    }

    private String message(Throwable ex) {
        String m = ex.getMessage();
        return StringUtils.hasText(m) ? m : ex.getClass().getSimpleName();
    }

    private String friendlyMessage(Exception ex) {
        if (ex instanceof MissingServletRequestParameterException e) {
            return "필수 파라미터가 없습니다: " + e.getParameterName();
        }
        if (ex instanceof HttpMessageNotReadableException) {
            return "요청 본문을 읽을 수 없습니다.";
        }
        return ExceptionCode.BAD_REQUEST.defaultMessage();
    }
}