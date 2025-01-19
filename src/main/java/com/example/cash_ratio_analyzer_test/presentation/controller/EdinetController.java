package com.example.cash_ratio_analyzer_test.presentation.controller;

import com.example.cash_ratio_analyzer_test.domain.model.FinancialDocument;
import com.example.cash_ratio_analyzer_test.application.service.EdinetScenarioService;
import com.example.cash_ratio_analyzer_test.application.service.FinancialDocumentService;
import com.example.cash_ratio_analyzer_test.presentation.controller.response.FinancialDocumentMetadataPostResponse;
import com.example.cash_ratio_analyzer_test.presentation.controller.response.FinancialDocumentPostResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/documents")
public class EdinetController {

    private final EdinetScenarioService edinetScenarioService;

    private final FinancialDocumentService financialDocumentService;

    public EdinetController(EdinetScenarioService edinetScenarioService, FinancialDocumentService financialDocumentService) {
        this.edinetScenarioService = edinetScenarioService;
        this.financialDocumentService = financialDocumentService;
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

    // TODO この処理はEdinet使用しないので別のクラスに移動する
    @GetMapping("/{documentId}")
    public FinancialDocument getFinancialDocument(@PathVariable String documentId) {
        return financialDocumentService.getFinancialDocument(documentId);
    }
}
