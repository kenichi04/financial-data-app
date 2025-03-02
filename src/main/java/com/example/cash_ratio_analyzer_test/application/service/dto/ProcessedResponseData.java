package com.example.cash_ratio_analyzer_test.application.service.dto;

import com.example.cash_ratio_analyzer_test.domain.model.Company;
import com.example.cash_ratio_analyzer_test.domain.model.DocumentMetadata;

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
