package com.example.cash_ratio_analyzer_test.application.service;

import com.example.cash_ratio_analyzer_test.application.service.enums.FetchMode;
import com.example.cash_ratio_analyzer_test.application.service.validation.ApiResponseValidator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
public class EdinetDocumentListService {

    /** 書類一覧API */
    @Value("${edinet.api.document.list.url:}")
    private String edinetDocumentListUrl;

    @Value("${edinet.api.subscriptionKey:}")
    private String subscriptionKey;

    private final ApiResponseValidator apiResponseValidator;

    public EdinetDocumentListService(ApiResponseValidator apiResponseValidator) {
        this.apiResponseValidator = apiResponseValidator;
    }

    public String fetchDocumentList(FetchMode mode, LocalDate fromDate) {
        var restTemplate = new RestTemplate();
        var formattedDate = fromDate.format(DateTimeFormatter.ofPattern("uuuu-MM-dd"));

        var response = restTemplate.exchange(
                edinetDocumentListUrl, HttpMethod.GET, null,
                String.class, formattedDate, mode.code(), subscriptionKey);

        apiResponseValidator.validateStatusCode(response.getStatusCode());
        apiResponseValidator.validateContentType(response.getHeaders().getContentType(),
                MediaType.APPLICATION_JSON);
        apiResponseValidator.validateResponseBody(response.getBody());

        return response.getBody();
    }
}
