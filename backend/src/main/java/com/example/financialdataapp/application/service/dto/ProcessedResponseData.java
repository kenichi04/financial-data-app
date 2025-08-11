package com.example.financialdataapp.application.service.dto;

import com.example.financialdataapp.domain.model.Company;
import com.example.financialdataapp.domain.model.DocumentMetadata;

import java.util.List;

public class ProcessedResponseData {
    private final List<Company> companies;
    private final List<DocumentMetadata> metadataList;

    public ProcessedResponseData(List<Company> companies, List<DocumentMetadata> metadataList) {
        this.companies = companies;
        this.metadataList = metadataList;
    }

    public List<Company> getCompanies() {
        return companies;
    }

    public List<DocumentMetadata> getMetadataList() {
        return metadataList;
    }
}
