package com.example.financialdataapp.application.service.exception;

/**
 * リクエストパラメータが業務ルール上不正な場合にスローされます。
 * プレゼンテーション層で 400 {@code INVALID_PARAMETER} に変換されます。
 * <p>
 * ドメイン層・パース処理が投げる {@link IllegalArgumentException} とは区別する。
 * 後者は「取り込んだデータが想定外」というサーバー側の問題であり、400にすべきではないため。
 */
public class InvalidParameterException extends RuntimeException {

    public InvalidParameterException(String message) {
        super(message);
    }
}
