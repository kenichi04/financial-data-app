package com.example.cash_ratio_analyzer.presentation.controller;

import com.example.cash_ratio_analyzer.application.service.financial.FinancialDocumentService;
import com.example.cash_ratio_analyzer.application.service.metadata.DocumentMetadataService;
import com.example.cash_ratio_analyzer.domain.model.Company;
import com.example.cash_ratio_analyzer.domain.model.DocumentMetadata;
import com.example.cash_ratio_analyzer.domain.model.FinancialDocument;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/financial-documents")
public class FinancialDocumentController {

    private final FinancialDocumentService financialDocumentService;

    private final DocumentMetadataService documentMetadataService;

    public FinancialDocumentController(FinancialDocumentService financialDocumentService, DocumentMetadataService documentMetadataService) {
        this.financialDocumentService = financialDocumentService;
        this.documentMetadataService = documentMetadataService;
    }

    @GetMapping("/{documentId}")
    public FinancialDocument getFinancialDocument(@PathVariable String documentId) {
        // TODO レスポンスモデルを作成する
        return financialDocumentService.getFinancialDocument(documentId);
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
