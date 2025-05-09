package com.example.financialdataapp.presentation.controller;

import com.example.financialdataapp.application.service.FinancialDocumentScenarioService;
import com.example.financialdataapp.application.service.DocumentMetadataScenarioService;
import com.example.financialdataapp.presentation.controller.response.DocumentMetadataPostResponse;
import com.example.financialdataapp.presentation.controller.response.FinancialDocumentPostResponse;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/edinet")
public class EdinetFetchController {

    private final DocumentMetadataScenarioService documentMetadataScenarioService;

    private final FinancialDocumentScenarioService financialDocumentScenarioService;

    public EdinetFetchController(DocumentMetadataScenarioService documentMetadataScenarioService, FinancialDocumentScenarioService financialDocumentScenarioService) {
        this.documentMetadataScenarioService = documentMetadataScenarioService;
        this.financialDocumentScenarioService = financialDocumentScenarioService;
    }

    @PostMapping("/metadata/fetch-and-save")
    public DocumentMetadataPostResponse fetchAndSaveDocumentMetadata(@RequestParam LocalDate fromDate) {
        var documentIds = documentMetadataScenarioService.fetchAndSaveDocumentMetadata(fromDate);

        var documentIdList = documentIds.stream()
                .map(documentId -> documentId.value())
                .toList();
        return new DocumentMetadataPostResponse(documentIdList);
    }

    @PostMapping("/{documentId}/fetch-and-save")
    public FinancialDocumentPostResponse fetchAndSaveFinancialData(@PathVariable String documentId) {
        var documentIdModel = financialDocumentScenarioService.fetchAndSaveFinancialData(documentId);
        return new FinancialDocumentPostResponse(documentIdModel.value());
    }
}
