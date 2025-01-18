package com.example.cash_ratio_analyzer_test.application.service.dto;

import com.example.cash_ratio_analyzer_test.domain.model.Company;
import com.example.cash_ratio_analyzer_test.domain.model.FinancialDocumentMetadata;

import java.util.List;

public class ProcessedResponseData {
    private final List<Company> companies;
    private final List<FinancialDocumentMetadata> metadataList;

    public ProcessedResponseData(List<Company> companies, List<FinancialDocumentMetadata> metadataList) {
        this.companies = companies;
        this.metadataList = metadataList;
    }

    public List<Company> getCompanies() {
        return companies;
    }

    public List<FinancialDocumentMetadata> getMetadataList() {
        return metadataList;
    }
}
