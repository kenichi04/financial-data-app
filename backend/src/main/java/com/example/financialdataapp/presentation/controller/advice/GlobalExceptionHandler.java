package com.example.financialdataapp.presentation.controller.advice;

import com.example.financialdataapp.application.service.exception.CompanyNotFoundException;
import com.example.financialdataapp.application.service.exception.InvalidParameterException;
import com.example.financialdataapp.presentation.controller.response.error.ErrorCode;
import com.example.financialdataapp.presentation.controller.response.error.ErrorResponse;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Path;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

/**
 * 全エンドポイント共通の例外ハンドラです。
 * 例外を docs/api_spec_draft.md「共通エラーレスポンス」の形式に変換します。
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * リクエストボディのバリデーション違反を400にマッピングします。
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValid(MethodArgumentNotValidException e) {
        String message = e.getBindingResult().getFieldErrors().stream()
                .findFirst()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .orElse(null);
        return build(ErrorCode.INVALID_PARAMETER, message);
    }

    /**
     * クエリパラメータ・パスパラメータのバリデーション違反（{@code @Validated} 経由）を400にマッピングします。
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolation(ConstraintViolationException e) {
        String message = e.getConstraintViolations().stream()
                .findFirst()
                .map(violation -> parameterName(violation.getPropertyPath()) + ": " + violation.getMessage())
                .orElse(null);
        return build(ErrorCode.INVALID_PARAMETER, message);
    }

    /**
     * パラメータの型変換失敗（例: 日付形式が不正）を400にマッピングします。
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleTypeMismatch(MethodArgumentTypeMismatchException e) {
        return build(ErrorCode.INVALID_PARAMETER, e.getName() + " の値が不正です: " + e.getValue());
    }

    /**
     * 必須クエリパラメータの欠落を400にマッピングします。
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorResponse> handleMissingParameter(MissingServletRequestParameterException e) {
        return build(ErrorCode.INVALID_PARAMETER, e.getParameterName() + " は必須です");
    }

    /**
     * 業務ルール上のパラメータ不正を400にマッピングします。
     */
    @ExceptionHandler(InvalidParameterException.class)
    public ResponseEntity<ErrorResponse> handleInvalidParameter(InvalidParameterException e) {
        return build(ErrorCode.INVALID_PARAMETER, e.getMessage());
    }

    /**
     * 企業が見つからない場合を404にマッピングします。
     * 例外メッセージは内部識別子を含むためレスポンスには載せず、既定メッセージを返します。
     */
    @ExceptionHandler(CompanyNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleCompanyNotFound(CompanyNotFoundException e) {
        return build(ErrorCode.COMPANY_NOT_FOUND, null);
    }

    /**
     * 既存コントローラが投げる {@link ResponseStatusException} を、ステータスに応じたコードに変換します。
     */
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ErrorResponse> handleResponseStatus(ResponseStatusException e) {
        ErrorCode errorCode = toErrorCode(e.getStatusCode());
        if (errorCode == ErrorCode.INTERNAL_SERVER_ERROR) {
            logger.error("Unhandled status exception", e);
            return build(errorCode, null);
        }
        return ResponseEntity.status(e.getStatusCode())
                .body(ErrorResponse.of(errorCode, e.getReason()));
    }

    /**
     * 存在しないパスへのリクエストを404にマッピングします。
     * （{@code Exception} ハンドラに拾われて500になるのを防ぐ）
     */
    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ErrorResponse> handleNoResourceFound(NoResourceFoundException e) {
        return build(ErrorCode.NOT_FOUND, null);
    }

    /**
     * 上記以外を500にマッピングします。内部情報を漏らさないためメッセージは固定文言とし、原因はログに残します。
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUnexpected(Exception e) {
        logger.error("Unexpected error", e);
        return build(ErrorCode.INTERNAL_SERVER_ERROR, null);
    }

    private ResponseEntity<ErrorResponse> build(ErrorCode errorCode, String message) {
        return ResponseEntity.status(errorCode.getStatus())
                .body(ErrorResponse.of(errorCode, message));
    }

    /**
     * プロパティパス（例: {@code search.size}）の末尾要素だけを取り出し、メソッド名が利用者に見えないようにします。
     */
    private String parameterName(Path propertyPath) {
        String leaf = null;
        for (Path.Node node : propertyPath) {
            leaf = node.getName();
        }
        return leaf == null ? propertyPath.toString() : leaf;
    }

    private ErrorCode toErrorCode(HttpStatusCode statusCode) {
        if (statusCode.isSameCodeAs(HttpStatus.NOT_FOUND)) {
            return ErrorCode.NOT_FOUND;
        }
        if (statusCode.isSameCodeAs(HttpStatus.BAD_REQUEST)) {
            return ErrorCode.INVALID_PARAMETER;
        }
        return ErrorCode.INTERNAL_SERVER_ERROR;
    }
}
