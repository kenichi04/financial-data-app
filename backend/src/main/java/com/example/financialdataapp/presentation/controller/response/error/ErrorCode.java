package com.example.financialdataapp.presentation.controller.response.error;

import org.springframework.http.HttpStatus;

/**
 * APIのエラーコードです。
 * コード体系とHTTPステータスの対応は docs/api_spec_draft.md「共通エラーレスポンス」に準拠します。
 * 認証・課金系のコード（UNAUTHORIZED / FORBIDDEN 等）はPhase 2で使用時に追加します。
 */
public enum ErrorCode {
    /** リクエストパラメータが不正 */
    INVALID_PARAMETER(HttpStatus.BAD_REQUEST, "リクエストパラメータが不正です"),
    /** 企業が見つからない */
    COMPANY_NOT_FOUND(HttpStatus.NOT_FOUND, "指定された企業が見つかりません"),
    /** リソースが見つからない（企業以外の汎用） */
    NOT_FOUND(HttpStatus.NOT_FOUND, "指定されたリソースが見つかりません"),
    /** サーバー内部エラー */
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "サーバー内部でエラーが発生しました");

    private final HttpStatus status;

    private final String defaultMessage;

    ErrorCode(HttpStatus status, String defaultMessage) {
        this.status = status;
        this.defaultMessage = defaultMessage;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public String getDefaultMessage() {
        return defaultMessage;
    }
}
