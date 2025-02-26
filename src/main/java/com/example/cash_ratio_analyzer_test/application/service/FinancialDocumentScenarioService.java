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

    /**
     * 指定された書類IDに基づいて財務データを取得し、分析して保存します。
     *
     * @param documentId 書類ID
     * @return 保存された財務書類のID
     */
    public DocumentId fetchAndSaveFinancialData(String documentId) {

        var fetchData = edinetDataFetchService.fetchFinancialData(FetchDocumentType.XBRL, documentId);
        // 一時ファイル作成して抽出する処理も検討する（将来的に）
        // TODO ターゲットファイルを可変長引数で指定できるようにする？
        var extractedFiles = edinetFileExtractionService.extractTargetFile(fetchData);

        var headerInfo = xbrlHeaderInfoExtractor.extractHeaderInfo(extractedFiles.getHeaderOrFirstMainContent());
        var financialDataList = xbrlFinancialDataExtractor.extractFinancialDataFromXbrl(extractedFiles.getTargetFileContent());

        // TODO サービス層で保存結果用の専用クラスを返すことも検討
        return financialDocumentService.saveFinancialDocument(documentId, headerInfo, financialDataList);
    }
}
