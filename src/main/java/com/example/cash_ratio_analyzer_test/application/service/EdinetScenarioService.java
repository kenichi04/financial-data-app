package com.example.cash_ratio_analyzer_test.application.service;

import com.example.cash_ratio_analyzer_test.application.service.enums.FetchMode;
import com.example.cash_ratio_analyzer_test.application.service.enums.FetchDocumentType;
import com.example.cash_ratio_analyzer_test.application.service.xbrl.XbrlFinancialDataExtractor;
import com.example.cash_ratio_analyzer_test.application.service.xbrl.XbrlHeaderInfoExtractor;
import com.example.cash_ratio_analyzer_test.domain.model.DocumentId;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

// TODO 2つのメソッドのユースケース違うので、クラス分けるべきかも
@Service
public class EdinetScenarioService {

    private final EdinetDocumentListService edinetDocumentListService;
    private final EdinetDataFetchService edinetDataFetchService;
    private final EdinetFileExtractionService edinetFileExtractionService;
    private final JsonParserService jsonParserService;
    private final XbrlHeaderInfoExtractor xbrlHeaderInfoExtractor;
    private final XbrlFinancialDataExtractor xbrlFinancialDataExtractor;
    private final FinancialDocumentMetadataService financialDocumentMetadataService;
    private final FinancialDocumentService financialDocumentService;

    public EdinetScenarioService(EdinetDocumentListService edinetDocumentListService, EdinetDataFetchService edinetDataFetchService, EdinetFileExtractionService edinetFileExtractionService, JsonParserService jsonParserService, XbrlHeaderInfoExtractor xbrlHeaderInfoExtractor, XbrlFinancialDataExtractor xbrlFinancialDataExtractor, FinancialDocumentMetadataService financialDocumentMetadataService, FinancialDocumentService financialDocumentService) {
        this.financialDocumentMetadataService = financialDocumentMetadataService;
        this.edinetDocumentListService = edinetDocumentListService;
        this.edinetDataFetchService = edinetDataFetchService;
        this.edinetFileExtractionService = edinetFileExtractionService;
        this.jsonParserService = jsonParserService;
        this.xbrlHeaderInfoExtractor = xbrlHeaderInfoExtractor;
        this.xbrlFinancialDataExtractor = xbrlFinancialDataExtractor;
        this.financialDocumentService = financialDocumentService;
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

    // TODO transactionalアノテーションを付与する
    // 書類取得APIから財務データ取得、分析して登録する処理を管理する
    public DocumentId fetchAndSaveFinancialData(String documentId) {

        var fetchData = edinetDataFetchService.fetchFinancialData(FetchDocumentType.XBRL, documentId);
        // 一時ファイル作成して抽出する処理も検討する（将来的に）
        // TODO ターゲットファイルを可変長引数で指定できるようにする？
        var extractedFiles = edinetFileExtractionService.extractTargetFile(fetchData);
        // TODO XBRLからヘッダ情報を抽出
        xbrlHeaderInfoExtractor.extractHeaderInfo(extractedFiles.getHeaderOrFirstMainContent());
        // XBRLから必要な財務データを抽出
        var extractedData = xbrlFinancialDataExtractor.extractFinancialDataFromXbrl(extractedFiles.getTargetFileContent());
        // TODO DBに保存
        // TODO サービス層で保存結果用の専用クラスを返すことも検討
        return financialDocumentService.saveFinancialData(documentId, extractedData);
    }
}
