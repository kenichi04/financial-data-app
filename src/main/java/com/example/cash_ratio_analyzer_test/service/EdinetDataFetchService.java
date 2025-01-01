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
     * Edinet APIからのレスポンスを取得します。
     *
     * @param type ドキュメントの種類
     * @param docNumber ドキュメント番号
     * @return Edinet APIからのレスポンス
     */
    public ResponseEntity<byte[]> getEdinetApiResponse(DocumentType type, String docNumber) {
        var restTemplate = new RestTemplate();
        var response = restTemplate.exchange(
                edinetApiUrl, HttpMethod.GET, null,
                byte[].class, docNumber, type.code(), subscriptionKey);

        validateStatusCode(response.getStatusCode());
        validateContentType(response.getHeaders().getContentType());
        return response;
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
}
