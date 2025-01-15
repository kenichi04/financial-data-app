package com.example.cash_ratio_analyzer_test.presentation.controller;

import com.example.cash_ratio_analyzer_test.domain.model.FinancialDocument;
import com.example.cash_ratio_analyzer_test.application.service.EdinetScenarioService;
import com.example.cash_ratio_analyzer_test.application.service.FinancialDocumentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

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
    public ResponseEntity<String> fetchAndSaveDocumentMetadata(@RequestParam LocalDate fromDate) {
        // シナリオサービスを呼び出す
        return edinetScenarioService.fetchAndSaveDocumentMetadata(fromDate);
    }

    @PostMapping("/financial-data/{documentId}/fetch")
    public ResponseEntity<FinancialDocument> fetchAndSaveFinancialData(@PathVariable String documentId) {
        // String testDocumentNumber = "S100TGZR";
        // シナリオサービスを呼び出す
        return edinetScenarioService.fetchAndSaveFinancialData(documentId);
    }

    // TODO この処理はEdinet使用しないので別のクラスに移動する
    @GetMapping("/{documentId}")
    public FinancialDocument getFinancialDocument(@PathVariable String documentId) {
        return financialDocumentService.getFinancialDocument(documentId);
    }
}
