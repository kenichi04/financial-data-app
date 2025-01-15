package com.example.cash_ratio_analyzer_test.domain.repository;

import com.example.cash_ratio_analyzer_test.domain.model.FinancialDocumentMetadata;

import java.util.List;

public interface IFinancialDocumentMetadataRepository {
    FinancialDocumentMetadata findByDocumentId(String documentId);
    void save(FinancialDocumentMetadata financialDocumentMetadata);
    void save(List<FinancialDocumentMetadata> metadataList);
}
