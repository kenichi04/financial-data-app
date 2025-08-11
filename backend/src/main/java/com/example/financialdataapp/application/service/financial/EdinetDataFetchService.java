package com.example.financialdataapp.application.service.financial;

import com.example.financialdataapp.application.service.enums.FetchDocumentType;
import com.example.financialdataapp.application.service.validation.ApiResponseValidator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * EDINETデータ取得サービスクラス。
 * EDINETのAPIを使用して文書データを取得します。
 */
@Service
public class EdinetDataFetchService {

    private final ApiResponseValidator apiResponseValidator;

    private final RestTemplate restTemplate;

    /** 書類取得API */
    private final String edinetDocumentRetrievalApiUrl;

    private final String subscriptionKey;

    public EdinetDataFetchService(
            ApiResponseValidator apiResponseValidator,
            RestTemplate restTemplate,
            @Value("${edinet.api.document.retrieval.url:}") String edinetDocumentRetrievalApiUrl,
            @Value("${edinet.api.subscriptionKey:}") String subscriptionKey) {
        this.apiResponseValidator = apiResponseValidator;
        this.restTemplate = restTemplate;
        this.edinetDocumentRetrievalApiUrl = edinetDocumentRetrievalApiUrl;
        this.subscriptionKey = subscriptionKey;
    }

    /**
     * 書類取得APIからデータを取得します。
     *
     * @param type 取得する書類の種類
     * @param documentId 取得する書類の書類番号
     * @return 取得したデータ
     */
    public byte[] fetchFinancialData(FetchDocumentType type, String documentId) {
        var response = restTemplate.exchange(
                edinetDocumentRetrievalApiUrl, HttpMethod.GET, null,
                byte[].class, documentId, type.code(), subscriptionKey);

        apiResponseValidator.validateStatusCode(response.getStatusCode());
        apiResponseValidator.validateContentType(response.getHeaders().getContentType(),
                MediaType.APPLICATION_OCTET_STREAM, MediaType.APPLICATION_PDF);
        apiResponseValidator.validateResponseBody(response.getBody());

        return response.getBody();
    }
}
