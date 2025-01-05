package com.example.cash_ratio_analyzer_test.service;

import com.example.cash_ratio_analyzer_test.DocumentType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class EdinetDataFetchService {

    // 書類取得API
    @Value("${edinet.api.url:}")
    private String edinetApiUrl;

    @Value("${edinet.api.subscriptionKey:}")
    private String subscriptionKey;

    /**
     * EDINET APIからデータを取得します。
     *
     * @param type     取得する書類の種類
     * @param documentNumber 取得する書類の書類番号
     * @return 取得したデータ
     */
    public byte[] fetchData(DocumentType type, String documentNumber) {
        var restTemplate = new RestTemplate();
        var response = restTemplate.exchange(
                edinetApiUrl, HttpMethod.GET, null,
                byte[].class, documentNumber, type.code(), subscriptionKey);

        validateStatusCode(response.getStatusCode());
        validateContentType(response.getHeaders().getContentType());
        validateResponseBody(response.getBody());

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
        return contentType.contains("application/octet-stream") || contentType.contains("application/pdf");
    }

    /**
     * レスポンスボディを検証します。
     *
     * @param responseBody 検証するレスポンスボディ
     * @throws RuntimeException レスポンスボディが無効な場合
     */
    private void validateResponseBody(byte[] responseBody) {
        if (responseBody == null || responseBody.length == 0) {
            throw new RuntimeException("Response body is empty or null");
        }
    }
}
