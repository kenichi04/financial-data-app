package com.example.cash_ratio_analyzer_test.infrastructure.repository;

import com.example.cash_ratio_analyzer_test.domain.model.FinancialDocumentMetadata;
import com.example.cash_ratio_analyzer_test.domain.repository.IFinancialDocumentMetadataRepository;

import java.util.HashMap;
import java.util.Map;

public class InMemoryFinancialDocumentMetadataRepository implements IFinancialDocumentMetadataRepository {
    private Map<String, FinancialDocumentMetadata> financialDocumentMetadataStore = new HashMap<>();

    @Override
    public FinancialDocumentMetadata findByDocumentId(String documentId) {
        return null;
    }

    @Override
    public void save(FinancialDocumentMetadata financialDocumentMetadata) {
        financialDocumentMetadataStore.put(financialDocumentMetadata.getDocumentId(), financialDocumentMetadata);
    }
}
