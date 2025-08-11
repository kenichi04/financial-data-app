package com.example.financialdataapp.application.service.metadata;

import com.example.financialdataapp.application.service.enums.FetchMode;
import com.example.financialdataapp.application.service.validation.ApiResponseValidator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * EDINETの書類一覧を取得するサービスクラス。
 */
@Service
public class EdinetDocumentListService {

    private final ApiResponseValidator apiResponseValidator;

    private final RestTemplate restTemplate;

    /** 書類一覧API */
    private final String edinetDocumentListUrl;

    private final String subscriptionKey;

    public EdinetDocumentListService(
            ApiResponseValidator apiResponseValidator,
            RestTemplate restTemplate,
            @Value("${edinet.api.document.list.url:}") String edinetDocumentListUrl,
            @Value("${edinet.api.subscriptionKey:}") String subscriptionKey) {
        this.apiResponseValidator = apiResponseValidator;
        this.restTemplate = restTemplate;
        this.edinetDocumentListUrl = edinetDocumentListUrl;
        this.subscriptionKey = subscriptionKey;
    }

    /**
     * EDINET APIから書類一覧（メタデータ）を取得します。
     *
     * @param mode     取得モード（例：METADATA_ONLY、METADATA_AND_LIST）
     * @param fromDate 取得開始日
     * @return 書類一覧を含むレスポンスボディのJSON形式の文字列
     */
    public String fetchDocumentList(FetchMode mode, LocalDate fromDate) {
        // TODO fromDateは当日以前、直近の財務局営業日の24時において10年を経過していない日付
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
