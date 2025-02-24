package com.example.cash_ratio_analyzer_test.infrastructure.repository.inmemory;

import com.example.cash_ratio_analyzer_test.domain.model.DocumentId;
import com.example.cash_ratio_analyzer_test.domain.model.FinancialDocumentMetadata;
import com.example.cash_ratio_analyzer_test.domain.repository.IFinancialDocumentMetadataRepository;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class InMemoryFinancialDocumentMetadataRepository implements IFinancialDocumentMetadataRepository {
    private Map<DocumentId, FinancialDocumentMetadata> metadataStore = new HashMap<>();

    @Override
    public FinancialDocumentMetadata findByDocumentId(DocumentId documentId) {
        return metadataStore.getOrDefault(documentId, null);
    }

    @Override
    public List<FinancialDocumentMetadata> findByDocumentIds(List<DocumentId> documentIdList) {
        return documentIdList.stream()
                .map(metadataStore::get)
                .filter(metadata -> metadata != null)
                .toList();
    }

    @Override
    public List<FinancialDocumentMetadata> findByProcessedFalse() {
        return metadataStore.values().stream()
                .filter(metadata -> !metadata.isProcessed())
                .toList();
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
