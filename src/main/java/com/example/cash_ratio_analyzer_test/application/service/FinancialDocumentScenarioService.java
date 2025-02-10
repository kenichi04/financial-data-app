package com.example.cash_ratio_analyzer_test.application.service;

import com.example.cash_ratio_analyzer_test.application.service.enums.FetchDocumentType;
import com.example.cash_ratio_analyzer_test.application.service.financial.EdinetDataFetchService;
import com.example.cash_ratio_analyzer_test.application.service.financial.EdinetFileExtractionService;
import com.example.cash_ratio_analyzer_test.application.service.financial.FinancialDocumentService;
import com.example.cash_ratio_analyzer_test.application.service.financial.xbrl.XbrlFinancialDataExtractor;
import com.example.cash_ratio_analyzer_test.application.service.financial.xbrl.XbrlHeaderInfoExtractor;
import com.example.cash_ratio_analyzer_test.domain.model.DocumentId;
import org.springframework.stereotype.Service;

@Service
public class FinancialDocumentScenarioService {

    private final EdinetDataFetchService edinetDataFetchService;
    private final EdinetFileExtractionService edinetFileExtractionService;
    private final XbrlHeaderInfoExtractor xbrlHeaderInfoExtractor;
    private final XbrlFinancialDataExtractor xbrlFinancialDataExtractor;
    private final FinancialDocumentService financialDocumentService;

    public FinancialDocumentScenarioService(EdinetDataFetchService edinetDataFetchService, EdinetFileExtractionService edinetFileExtractionService, XbrlHeaderInfoExtractor xbrlHeaderInfoExtractor, XbrlFinancialDataExtractor xbrlFinancialDataExtractor, FinancialDocumentService financialDocumentService) {
        this.edinetDataFetchService = edinetDataFetchService;
        this.edinetFileExtractionService = edinetFileExtractionService;
        this.xbrlHeaderInfoExtractor = xbrlHeaderInfoExtractor;
        this.xbrlFinancialDataExtractor = xbrlFinancialDataExtractor;
        this.financialDocumentService = financialDocumentService;
    }

    // TODO transactionalアノテーションを付与する
    // 書類取得APIから財務データ取得、分析して登録する処理を管理する
    public DocumentId fetchAndSaveFinancialData(String documentId) {

        var fetchData = edinetDataFetchService.fetchFinancialData(FetchDocumentType.XBRL, documentId);
        // 一時ファイル作成して抽出する処理も検討する（将来的に）
        // TODO ターゲットファイルを可変長引数で指定できるようにする？
        var extractedFiles = edinetFileExtractionService.extractTargetFile(fetchData);
        // XBRLからヘッダ情報を抽出
        var headerInfo = xbrlHeaderInfoExtractor.extractHeaderInfo(extractedFiles.getHeaderOrFirstMainContent());
        // XBRLから必要な財務データを抽出
        var extractedData = xbrlFinancialDataExtractor.extractFinancialDataFromXbrl(extractedFiles.getTargetFileContent());
        // TODO DBに保存
        // TODO サービス層で保存結果用の専用クラスを返すことも検討
        return financialDocumentService.saveFinancialDocument(documentId, headerInfo, extractedData);
    }
}
