package com.example.cash_ratio_analyzer_test.infrastructure.repository;

import com.example.cash_ratio_analyzer_test.domain.model.FinancialDocumentMetadata;
import com.example.cash_ratio_analyzer_test.domain.repository.IFinancialDocumentMetadataRepository;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class InMemoryFinancialDocumentMetadataRepository implements IFinancialDocumentMetadataRepository {
    private Map<String, FinancialDocumentMetadata> metadataStore = new HashMap<>();

    @Override
    public FinancialDocumentMetadata findByDocumentId(String documentId) {
        return metadataStore.getOrDefault(documentId, null);
    }

    @Override
    public void save(FinancialDocumentMetadata financialDocumentMetadata) {
        metadataStore.put(financialDocumentMetadata.getDocumentId(), financialDocumentMetadata);
    }

    @Override
    public void save(List<FinancialDocumentMetadata> metadataList) {
        metadataList.forEach(metadata ->
                metadataStore.put(metadata.getDocumentId(), metadata));
    }
}
