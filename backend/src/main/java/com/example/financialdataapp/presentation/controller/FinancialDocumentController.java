package com.example.financialdataapp.presentation.controller;

import com.example.financialdataapp.application.service.FinancialDocumentQueryUseCase;
import com.example.financialdataapp.application.service.dto.FinancialDocumentDto;
import com.example.financialdataapp.application.service.financial.FinancialDocumentService;
import com.example.financialdataapp.application.service.metadata.DocumentMetadataService;
import com.example.financialdataapp.domain.model.Company;
import com.example.financialdataapp.domain.model.DocumentMetadata;
import com.example.financialdataapp.domain.model.FinancialDocument;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/financial-documents")
public class FinancialDocumentController {

    private final FinancialDocumentQueryUseCase financialDocumentQueryUseCase;

    private final FinancialDocumentService financialDocumentService;

    public FinancialDocumentController(FinancialDocumentQueryUseCase financialDocumentQueryUseCase, FinancialDocumentService financialDocumentService) {
        this.financialDocumentQueryUseCase = financialDocumentQueryUseCase;
        this.financialDocumentService = financialDocumentService;
    }

    @Deprecated
    @GetMapping("/v1/{documentId}")
    public FinancialDocument getFinancialDocument(@PathVariable String documentId) {
        return financialDocumentService.getFinancialDocument(documentId);
    }

    @GetMapping("/{documentId}")
    public FinancialDocumentDto get(@PathVariable String documentId) {
        return financialDocumentQueryUseCase.getFinancialDocumentDto(documentId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Document not found: " + documentId));
    }
}
