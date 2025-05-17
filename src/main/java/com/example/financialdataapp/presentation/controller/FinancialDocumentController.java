package com.example.financialdataapp.presentation.controller;

import com.example.financialdataapp.application.service.FinancialDocumentQueryUseCase;
import com.example.financialdataapp.application.service.dto.FinancialDocumentDto;
import com.example.financialdataapp.application.service.financial.FinancialDocumentService;
import com.example.financialdataapp.application.service.metadata.DocumentMetadataService;
import com.example.financialdataapp.domain.model.Company;
import com.example.financialdataapp.domain.model.DocumentMetadata;
import com.example.financialdataapp.domain.model.FinancialDocument;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/financial-documents")
public class FinancialDocumentController {

    private final FinancialDocumentQueryUseCase financialDocumentQueryUseCase;

    private final FinancialDocumentService financialDocumentService;

    private final DocumentMetadataService documentMetadataService;

    public FinancialDocumentController(FinancialDocumentQueryUseCase financialDocumentQueryUseCase, FinancialDocumentService financialDocumentService, DocumentMetadataService documentMetadataService) {
        this.financialDocumentQueryUseCase = financialDocumentQueryUseCase;
        this.financialDocumentService = financialDocumentService;
        this.documentMetadataService = documentMetadataService;
    }

    @Deprecated
    @GetMapping("/v1/{documentId}")
    public FinancialDocument getFinancialDocument(@PathVariable String documentId) {
        return financialDocumentService.getFinancialDocument(documentId);
    }

    @GetMapping("/{documentId}")
    public FinancialDocumentDto get(@PathVariable String documentId) {
        return financialDocumentQueryUseCase.getFinancialDocumentDto(documentId);
    }

    // 以下2つは同じ集約に属する（DocumentMetadata関連）
    @GetMapping("/unprocessedMetadata")
    public List<DocumentMetadata> getUnprocessedMetadata() {
        // TODO レスポンスモデルを作成する
        return documentMetadataService.getUnprocessedMetadata();
    }

    @GetMapping("/companies")
    public List<Company> getCompanies() {
        // TODO レスポンスモデルを作成する
        return documentMetadataService.getCompanies();
    }
}
