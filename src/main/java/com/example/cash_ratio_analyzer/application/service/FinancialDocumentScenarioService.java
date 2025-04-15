package com.example.cash_ratio_analyzer.application.service;

import com.example.cash_ratio_analyzer.application.service.enums.FetchDocumentType;
import com.example.cash_ratio_analyzer.application.service.financial.EdinetDataFetchService;
import com.example.cash_ratio_analyzer.application.service.financial.EdinetFileExtractionService;
import com.example.cash_ratio_analyzer.application.service.financial.FinancialDocumentService;
import com.example.cash_ratio_analyzer.application.service.financial.xbrl.XbrlFinancialDataExtractor;
import com.example.cash_ratio_analyzer.application.service.financial.xbrl.XbrlHeaderInfoExtractor;
import com.example.cash_ratio_analyzer.domain.model.DocumentId;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

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
        if (financialDocumentService.existsByDocumentId(documentId)) {
            throw new IllegalArgumentException("Document already exists: " + documentId);
        }

        var fetchData = edinetDataFetchService.fetchFinancialData(FetchDocumentType.XBRL, documentId);
        // TODO 一時ファイル作成して抽出する処理も検討する
        var extractedFiles = edinetFileExtractionService.extractTargetFile(fetchData);
        // TODO 検討. 先にbyte→文字列などに変換して、必要な要素があるか確認（ファイルのフィルタリング）

        var headerInfo = xbrlHeaderInfoExtractor.extractHeaderInfo(extractedFiles.getHeaderOrFirstMainContent());

        var targetContents = extractedFiles.getTargetFiles().stream().map(x -> x.content());
        var allFinancialData = targetContents
                .flatMap(content -> xbrlFinancialDataExtractor.extractFinancialDataFromXbrl(content).stream())
                .collect(Collectors.toList());

        // TODO サービス層で保存結果用の専用クラスを返すことも検討
        return financialDocumentService.createFinancialDocument(documentId, headerInfo, allFinancialData);
    }
}
