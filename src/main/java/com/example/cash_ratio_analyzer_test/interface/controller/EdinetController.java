package com.example.cash_ratio_analyzer_test.controller;

import com.example.cash_ratio_analyzer_test.domain.model.FinancialData;
import com.example.cash_ratio_analyzer_test.domain.model.FinancialDocument;
import com.example.cash_ratio_analyzer_test.application.service.EdinetScenarioService;
import com.example.cash_ratio_analyzer_test.application.service.FinancialDocumentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/edinet")
public class EdinetController {

    private final EdinetScenarioService edinetScenarioService;

    private final FinancialDocumentService financialDocumentService;

    public EdinetController(EdinetScenarioService edinetScenarioService, FinancialDocumentService financialDocumentService) {
        this.edinetScenarioService = edinetScenarioService;
        this.financialDocumentService = financialDocumentService;
    }

    @GetMapping("/fetchAndAnalyze/{documentId}")
    public ResponseEntity<List<FinancialData>> fetchAndAnalyzeData(@PathVariable String documentId) {
        // String testDocumentNumber = "S100TGZR";
        // シナリオサービスを呼び出す
        return edinetScenarioService.executeEdinetScenario(documentId);
    }

    @GetMapping("/{documentId}")
    public FinancialDocument getFinancialDocument(@PathVariable String documentId) {
        return financialDocumentService.getFinancialDocument(documentId);
    }
}
