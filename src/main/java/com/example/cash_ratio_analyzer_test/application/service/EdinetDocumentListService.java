package com.example.cash_ratio_analyzer_test.application.service;

import com.example.cash_ratio_analyzer_test.application.service.enums.FetchMode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class EdinetDocumentListService {

    /** 書類一覧API */
    @Value("${edinet.api.document.list.url:}")
    private String edinetDocumentListUrl;

    @Value("${edinet.api.subscriptionKey:}")
    private String subscriptionKey;

    public String fetchDocumentList(FetchMode mode) {
        var restTemplate = new RestTemplate();
        // TODO JSON形式の文字列
        // TODO dateは必須パラメータ > 引数で受け取る

        var response = restTemplate.exchange(
                edinetDocumentListUrl, HttpMethod.GET, null,
                String.class, "2023-04-03", mode.code(), subscriptionKey);

        validateStatusCode(response.getStatusCode());
        validateContentType(response.getHeaders().getContentType());
        validateResponseBody(response.getBody());
        // TODO JSONパース

        return response.getBody();
    }

    /**
     * ステータスコードを検証します。
     *
     * @param statusCode 検証するステータスコード
     * @throws RuntimeException ステータスコードが2xx系でない場合
     */
    private void validateStatusCode(HttpStatusCode statusCode) {
        if (!statusCode.is2xxSuccessful()) {
            throw new RuntimeException("Failed to fetch data from Edinet API. status code: " + statusCode);
        }
    }

    /**
     * コンテンツタイプを検証します。
     *
     * @param contentType 検証するコンテンツタイプ
     * @throws RuntimeException コンテンツタイプが無効な場合
     */
    private void validateContentType(MediaType contentType) {
        if (contentType == null || !isValidContentType(contentType.toString())) {
            throw new RuntimeException("Invalid content type: " + contentType);
        }
    }

    /**
     * コンテンツタイプが有効かどうかを検証します。
     *
     * @param contentType 検証するコンテンツタイプ
     * @return コンテンツタイプが有効な場合はtrue、そうでない場合はfalse
     */
    private boolean isValidContentType(String contentType) {
        return contentType.contains("application/json");
    }

    /**
     * レスポンスボディを検証します。
     *
     * @param responseBody 検証するレスポンスボディ
     * @throws RuntimeException レスポンスボディが無効な場合
     */
    private void validateResponseBody(String responseBody) {
        if (responseBody == null || responseBody.length() == 0) {
            throw new RuntimeException("Response body is empty or null");
        }
    }
}
