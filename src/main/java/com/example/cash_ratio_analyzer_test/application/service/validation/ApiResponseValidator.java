package com.example.cash_ratio_analyzer_test.application.service.validation;

import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

@Component
public class ApiResponseValidator {

    /**
     * ステータスコードを検証します。
     *
     * @param statusCode 検証するステータスコード
     * @throws RuntimeException ステータスコードが2xx系でない場合
     */
    public void validateStatusCode(HttpStatusCode statusCode) {
        if (!statusCode.is2xxSuccessful()) {
            throw new RuntimeException("Failed to fetch data from Edinet API. status code: " + statusCode);
        }
    }

    /**
     * コンテンツタイプを検証します。
     *
     * @param contentType 実際のコンテンツタイプ
     * @param expectedContentType 期待されるコンテンツタイプの可変長引数
     * @throws RuntimeException コンテンツタイプが期待されるものと一致しない場合
     */
    public void validateContentType(MediaType contentType, String... expectedContentType) {
        if (contentType == null || !isValidContentType(contentType.toString(), expectedContentType)) {
            throw new RuntimeException("Invalid content type: " + contentType);
        }
    }

    /**
     * 実際のコンテンツタイプが期待されるコンテンツタイプのいずれかを含むかどうかを検証します。
     *
     * @param actualContentType 実際のコンテンツタイプ
     * @param expectedContentType 期待されるコンテンツタイプの可変長引数
     * @return 実際のコンテンツタイプが期待されるコンテンツタイプのいずれかを含む場合はtrue、それ以外の場合はfalse
     */
    private boolean isValidContentType(String actualContentType, String... expectedContentType) {
        for (var expected : expectedContentType) {
            if (actualContentType.contains(expected)) {
                return true;
            }
        }
        return false;
    }

    /**
     * レスポンスボディを検証します。
     *
     * @param responseBody 検証するレスポンスボディ
     * @throws RuntimeException レスポンスボディがnullまたは空の場合
     */
    public void validateResponseBody(String responseBody) {
        if (responseBody == null || responseBody.length() == 0) {
            throw new RuntimeException("Response body is empty or null");
        }
    }

    /**
     * レスポンスボディを検証します。
     *
     * @param responseBody 検証するレスポンスボディ
     * @throws RuntimeException レスポンスボディがnullまたは空の場合
     */
    public void validateResponseBody(byte[] responseBody) {
        if (responseBody == null || responseBody.length == 0) {
            throw new RuntimeException("Response body is empty or null");
        }
    }
}
