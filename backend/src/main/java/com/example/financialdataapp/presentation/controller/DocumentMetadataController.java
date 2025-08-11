package com.example.financialdataapp.presentation.controller;

import com.example.financialdataapp.application.service.DocumentMetadataQueryUseCase;
import com.example.financialdataapp.application.service.dto.CompanyDto;
import com.example.financialdataapp.application.service.dto.DocumentMetadataDto;
import com.example.financialdataapp.application.service.metadata.DocumentMetadataService;
import com.example.financialdataapp.domain.model.Company;
import com.example.financialdataapp.domain.model.DocumentMetadata;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/document-metadata")
public class DocumentMetadataController {

    private final DocumentMetadataQueryUseCase documentMetadataQueryUseCase;

    private final DocumentMetadataService documentMetadataService;

    public DocumentMetadataController(DocumentMetadataQueryUseCase documentMetadataQueryUseCase, DocumentMetadataService documentMetadataService) {
        this.documentMetadataQueryUseCase = documentMetadataQueryUseCase;
        this.documentMetadataService = documentMetadataService;
    }

    @Deprecated
    @GetMapping("/v1/unprocessedMetadata")
    public List<DocumentMetadata> getUnprocessedDocumentMetadata() {
        return documentMetadataService.getUnprocessedMetadata();
    }

    @GetMapping("/unprocessedMetadata")
    public List<DocumentMetadataDto> getUnprocessedMetadata() {
        return documentMetadataQueryUseCase.getUnProcessedMetadata();
    }

    @Deprecated
    @GetMapping("/v1/companies")
    public List<Company> getCompanies() {
        return documentMetadataService.getCompanies();
    }

    @GetMapping("/companies")
    public List<CompanyDto> getAllCompanies() {
        return documentMetadataQueryUseCase.getCompanies();
    }
}
