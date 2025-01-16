package com.example.cash_ratio_analyzer_test.application.service;

import com.example.cash_ratio_analyzer_test.application.service.enums.FetchMode;
import com.example.cash_ratio_analyzer_test.application.service.enums.FetchDocumentType;
import com.example.cash_ratio_analyzer_test.domain.model.FinancialDocument;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

// TODO 2つのメソッドのユースケース違うので、クラス分けても良いかも
@Service
public class EdinetScenarioService {

    private final EdinetDocumentListService edinetDocumentListService;
    private final EdinetDataFetchService edinetDataFetchService;
    private final EdinetDataParsingService edinetDataParsingService;
    private final JsonParserService jsonParserService;
    private final XbrlParserService xbrlParserService;
    private final FinancialDocumentMetadataService financialDocumentMetadataService;
    private final FinancialDocumentService financialDocumentService;

    public EdinetScenarioService(EdinetDocumentListService edinetDocumentListService, EdinetDataFetchService edinetDataFetchService, EdinetDataParsingService edinetDataParsingService, JsonParserService jsonParserService, XbrlParserService xbrlParserService, FinancialDocumentMetadataService financialDocumentMetadataService, FinancialDocumentService financialDocumentService) {
        this.financialDocumentMetadataService = financialDocumentMetadataService;
        this.edinetDocumentListService = edinetDocumentListService;
        this.edinetDataFetchService = edinetDataFetchService;
        this.edinetDataParsingService = edinetDataParsingService;
        this.jsonParserService = jsonParserService;
        this.xbrlParserService = xbrlParserService;
        this.financialDocumentService = financialDocumentService;
    }

    // TODO transactionalアノテーションを付与する
    // 書類一覧APIから書類メタデータ取得、登録する処理を管理する
    public ResponseEntity<String> fetchAndSaveDocumentMetadata(LocalDate fromDate) {

        var data = edinetDocumentListService.fetchDocumentList(FetchMode.METADATA_AND_LIST, fromDate);
        // TODO companyはここで抽出・登録せず、事前登録する想定
        var metadataList = jsonParserService.parseDocumentList(data);

        // TODO DBに保存
        financialDocumentMetadataService.createMetadata(metadataList);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body("Document metadata processed successfully.");
    }

    // TODO transactionalアノテーションを付与する
    // 書類取得APIから財務データ取得、分析して登録する処理を管理する
    public ResponseEntity<FinancialDocument> fetchAndSaveFinancialData(String documentId) {
        var fetchData = edinetDataFetchService.fetchFinancialData(FetchDocumentType.XBRL, documentId);
        // MapかListで返すようにしても良いかも（対象が複数ある場合）
        // 一時ファイル作成も検討する（将来的に）
        var targetData = edinetDataParsingService.extractTargetFile(fetchData);
        // XBRLから必要なデータを抽出
        var extractedData = xbrlParserService.parseXbrl(targetData);
        // TODO DBに保存
        financialDocumentService.saveFinancialData(documentId, extractedData);

        var storedData = financialDocumentService.getFinancialDocument(documentId);
        return ResponseEntity.status(HttpStatus.CREATED).body(storedData);
    }
}
