package com.example.financialdataapp.application.service;

import com.example.financialdataapp.application.service.dto.CompanyDto;
import com.example.financialdataapp.application.service.metadata.ICompanyQueryService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DocumentMetadataQueryUseCase {

    private final ICompanyQueryService companyQueryService;

    public DocumentMetadataQueryUseCase(ICompanyQueryService companyQueryService) {
        this.companyQueryService = companyQueryService;
    }

    public List<CompanyDto> getCompanies() {
        return companyQueryService.fetchAllCompanies();
    }
}
