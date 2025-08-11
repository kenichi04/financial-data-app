package com.example.financialdataapp.application.service;

import com.example.financialdataapp.application.service.dto.CompanyDto;
import com.example.financialdataapp.application.service.dto.DocumentMetadataDto;
import com.example.financialdataapp.application.service.metadata.ICompanyQueryService;
import com.example.financialdataapp.application.service.metadata.IDocumentMetadataQueryService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DocumentMetadataQueryUseCase {

    private final IDocumentMetadataQueryService documentMetadataQueryService;

    private final ICompanyQueryService companyQueryService;

    public DocumentMetadataQueryUseCase(IDocumentMetadataQueryService documentMetadataQueryService, ICompanyQueryService companyQueryService) {
        this.documentMetadataQueryService = documentMetadataQueryService;
        this.companyQueryService = companyQueryService;
    }

    public List<DocumentMetadataDto> getUnProcessedMetadata() {
        return documentMetadataQueryService.fetchUnProcessedMetadata();
    }

    public List<CompanyDto> getCompanies() {
        return companyQueryService.fetchAllCompanies();
    }
}
