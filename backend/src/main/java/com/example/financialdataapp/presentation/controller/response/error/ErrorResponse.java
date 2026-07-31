package com.example.financialdataapp.presentation.controller.response.error;

/**
 * 全エンドポイント共通のエラーレスポンスです。
 * <pre>
 * {"error": {"code": "COMPANY_NOT_FOUND", "message": "指定された企業が見つかりません"}}
 * </pre>
 *
 * @param error エラー本体
 */
public record ErrorResponse(Body error) {

    /**
     * エラー本体です。
     *
     * @param code    エラーコード
     * @param message 利用者向けのメッセージ
     */
    public record Body(String code, String message) {
    }

    /**
     * エラーコードの既定メッセージでレスポンスを生成します。
     *
     * @param errorCode エラーコード
     * @return エラーレスポンス
     */
    public static ErrorResponse of(ErrorCode errorCode) {
        return of(errorCode, errorCode.getDefaultMessage());
    }

    /**
     * メッセージを指定してレスポンスを生成します。
     *
     * @param errorCode エラーコード
     * @param message   利用者向けのメッセージ。nullまたは空文字の場合は既定メッセージを使う
     * @return エラーレスポンス
     */
    public static ErrorResponse of(ErrorCode errorCode, String message) {
        String resolved = (message == null || message.isBlank()) ? errorCode.getDefaultMessage() : message;
        return new ErrorResponse(new Body(errorCode.name(), resolved));
    }
}
