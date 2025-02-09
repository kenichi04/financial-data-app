package com.example.cash_ratio_analyzer_test.application.service;

import com.example.cash_ratio_analyzer_test.application.service.enums.FetchMode;
import com.example.cash_ratio_analyzer_test.domain.model.DocumentId;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class DocumentMetadataScenarioService {

    private final EdinetDocumentListService edinetDocumentListService;
    private final JsonParserService jsonParserService;
    private final FinancialDocumentMetadataService financialDocumentMetadataService;

    public DocumentMetadataScenarioService(EdinetDocumentListService edinetDocumentListService, JsonParserService jsonParserService, FinancialDocumentMetadataService financialDocumentMetadataService) {
        this.financialDocumentMetadataService = financialDocumentMetadataService;
        this.edinetDocumentListService = edinetDocumentListService;
        this.jsonParserService = jsonParserService;
    }

    // TODO transactionalアノテーションを付与する
    // 書類一覧APIから書類メタデータ取得、登録する処理を管理する
    public List<DocumentId> fetchAndSaveDocumentMetadata(LocalDate fromDate) {

        var data = edinetDocumentListService.fetchDocumentList(FetchMode.METADATA_AND_LIST, fromDate);
        // metadataおよびcompanyを作成
        var processedResponseData = jsonParserService.parseDocumentList(data);

        // TODO DBに保存
        // TODO サービス層で保存結果用の専用クラスを返すことも検討
        return financialDocumentMetadataService.createMetadata(processedResponseData);
    }
}
