package com.example.cash_ratio_analyzer_test.presentation.controller;

import com.example.cash_ratio_analyzer_test.application.service.FinancialDocumentMetadataService;
import com.example.cash_ratio_analyzer_test.domain.model.Company;
import com.example.cash_ratio_analyzer_test.domain.model.FinancialDocument;
import com.example.cash_ratio_analyzer_test.application.service.EdinetScenarioService;
import com.example.cash_ratio_analyzer_test.application.service.FinancialDocumentService;
import com.example.cash_ratio_analyzer_test.domain.model.FinancialDocumentMetadata;
import com.example.cash_ratio_analyzer_test.presentation.controller.response.FinancialDocumentMetadataPostResponse;
import com.example.cash_ratio_analyzer_test.presentation.controller.response.FinancialDocumentPostResponse;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/documents")
public class EdinetController {

    private final EdinetScenarioService edinetScenarioService;

    private final FinancialDocumentService financialDocumentService;

    private final FinancialDocumentMetadataService financialDocumentMetadataService;

    public EdinetController(EdinetScenarioService edinetScenarioService, FinancialDocumentService financialDocumentService, FinancialDocumentMetadataService financialDocumentMetadataService) {
        this.edinetScenarioService = edinetScenarioService;
        this.financialDocumentService = financialDocumentService;
        this.financialDocumentMetadataService = financialDocumentMetadataService;
    }

    @PostMapping("/metadata/fetch")
    public FinancialDocumentMetadataPostResponse fetchAndSaveDocumentMetadata(@RequestParam LocalDate fromDate) {
        var documentIds = edinetScenarioService.fetchAndSaveDocumentMetadata(fromDate);

        var documentIdList = documentIds.stream()
                .map(documentId -> documentId.value())
                .toList();
        return new FinancialDocumentMetadataPostResponse(documentIdList);
    }

    @PostMapping("/financial-data/{documentId}/fetch")
    public FinancialDocumentPostResponse fetchAndSaveFinancialData(@PathVariable String documentId) {
        // String testDocumentNumber = "S100TGZR";
        var documentIdModel = edinetScenarioService.fetchAndSaveFinancialData(documentId);
        return new FinancialDocumentPostResponse(documentIdModel.value());
    }

    // TODO ここから下の処理はEdinet使用しないので別のクラスに移動する
    @GetMapping("/{documentId}")
    public FinancialDocument getFinancialDocument(@PathVariable String documentId) {
        // TODO レスポンスモデルを作成する
        return financialDocumentService.getFinancialDocument(documentId);
    }

    @GetMapping("/unprocessedMetadata")
    public List<FinancialDocumentMetadata> getUnprocessedMetadata() {
        // TODO レスポンスモデルを作成する
        return financialDocumentMetadataService.getUnprocessedMetadata();
    }

    @GetMapping("/companies")
    public List<Company> getCompanies() {
        // TODO レスポンスモデルを作成する
        return financialDocumentMetadataService.getCompanies();
    }
}
