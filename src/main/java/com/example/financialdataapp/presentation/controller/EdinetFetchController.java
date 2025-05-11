package com.example.financialdataapp.presentation.controller;

import com.example.financialdataapp.application.service.FinancialDocumentFetchUseCase;
import com.example.financialdataapp.application.service.DocumentMetadataFetchUseCase;
import com.example.financialdataapp.presentation.controller.response.DocumentMetadataPostResponse;
import com.example.financialdataapp.presentation.controller.response.FinancialDocumentPostResponse;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/edinet")
public class EdinetFetchController {

    private final DocumentMetadataFetchUseCase documentMetadataFetchUseCase;

    private final FinancialDocumentFetchUseCase financialDocumentFetchUseCase;

    public EdinetFetchController(DocumentMetadataFetchUseCase documentMetadataFetchUseCase, FinancialDocumentFetchUseCase financialDocumentFetchUseCase) {
        this.documentMetadataFetchUseCase = documentMetadataFetchUseCase;
        this.financialDocumentFetchUseCase = financialDocumentFetchUseCase;
    }

    @PostMapping("/metadata/fetch-and-save")
    public DocumentMetadataPostResponse fetchAndSaveDocumentMetadata(@RequestParam LocalDate fromDate) {
        var documentIds = documentMetadataFetchUseCase.fetchAndSaveDocumentMetadata(fromDate);

        var documentIdList = documentIds.stream()
                .map(documentId -> documentId.value())
                .toList();
        return new DocumentMetadataPostResponse(documentIdList);
    }

    @PostMapping("/{documentId}/fetch-and-save")
    public FinancialDocumentPostResponse fetchAndSaveFinancialData(@PathVariable String documentId) {
        var documentIdModel = financialDocumentFetchUseCase.fetchAndSaveFinancialData(documentId);
        return new FinancialDocumentPostResponse(documentIdModel.value());
    }
}
