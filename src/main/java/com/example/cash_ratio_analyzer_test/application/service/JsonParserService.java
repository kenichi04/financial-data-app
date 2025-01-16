package com.example.cash_ratio_analyzer_test.application.service;

import com.example.cash_ratio_analyzer_test.application.service.dto.DocumentListResponse;
import com.example.cash_ratio_analyzer_test.domain.enums.EdinetDocumentType;
import com.example.cash_ratio_analyzer_test.domain.model.FinancialDocumentMetadata;
import com.example.cash_ratio_analyzer_test.domain.service.DocumentService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * EDINET書類一覧APIレスポンスのJSONデータを解析するサービスクラス。
 */
@Service
public class JsonParserService {

    private final DocumentService documentService;

    public JsonParserService(DocumentService documentService) {
        this.documentService = documentService;
    }

    /**
     * JSONデータを解析し、許可されたドキュメントタイプのFinancialDocumentMetadataリストを返します。
     *
     * @param jsonData JSON形式のデータ
     * @return 許可されたドキュメントタイプのFinancialDocumentMetadataリスト
     * @throws RuntimeException JSONデータの解析中にエラーが発生した場合
     */
    public List<FinancialDocumentMetadata> parseDocumentList(String jsonData) {
        var response = parseJsonData(jsonData);

        validateResponseStatus(response.getStatus());
        if (response.getCount() == 0 || response.getResults() == null) {
            return List.of();
        }

        var metadataList = response.getResults().stream()
                .filter(result -> documentService.isPermittedDocumentType(result.getDocTypeCode()))
                .map(result -> {
                    var documentType = EdinetDocumentType.fromCode(
                            Integer.parseInt(result.getDocTypeCode()));
                    var submissionDate = LocalDate.parse(result.getSubmitDateTime(),
                            DateTimeFormatter.ofPattern("uuuu-MM-dd HH:mm"));

                    return new FinancialDocumentMetadata(
                            result.getDocID(),
                            result.getDocDescription(),
                            result.getEdinetCode(),
                            // 提出会社名はデバッグ用に取得しておく
                            result.getFilerName(),
                            documentType,
                            submissionDate);
                }).toList();

        return metadataList;
    }

    /**
     * JSONデータをDocumentListResponseオブジェクトに変換します。
     *
     * @param jsonData JSON形式のデータ
     * @return DocumentListResponseオブジェクト
     * @throws RuntimeException JSONデータのマッピングまたは処理中にエラーが発生した場合
     */
    private DocumentListResponse parseJsonData(String jsonData) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(jsonData, DocumentListResponse.class);
        } catch (JsonMappingException e) {
            throw new RuntimeException("Error mapping JSON data: " + e.getMessage(), e);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error processing JSON data: " + e.getMessage(), e);
        }
    }

    /**
     * レスポンスのステータスを検証します。
     *
     * @param status レスポンスのステータスコード
     * @throws RuntimeException ステータスがnullまたはHttpStatus.OKでない場合
     */
    private void validateResponseStatus(String status) {
        if (status == null || !status.equals(String.valueOf(HttpStatus.OK.value()))) {
            throw new RuntimeException("Failed to fetch data from Edinet API. status code: " + status);
        }
    }
}
