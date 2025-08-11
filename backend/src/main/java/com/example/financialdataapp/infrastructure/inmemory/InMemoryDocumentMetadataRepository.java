package com.example.financialdataapp.infrastructure.inmemory;

import com.example.financialdataapp.domain.model.DocumentId;
import com.example.financialdataapp.domain.model.DocumentMetadata;
import com.example.financialdataapp.domain.repository.IDocumentMetadataRepository;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class InMemoryDocumentMetadataRepository implements IDocumentMetadataRepository {
    private Map<DocumentId, DocumentMetadata> metadataStore = new HashMap<>();

    @Override
    public DocumentMetadata findByDocumentId(DocumentId documentId) {
        return metadataStore.getOrDefault(documentId, null);
    }

    @Override
    public List<DocumentMetadata> findByDocumentIds(List<DocumentId> documentIdList) {
        return documentIdList.stream()
                .map(metadataStore::get)
                .filter(metadata -> metadata != null)
                .toList();
    }

    @Override
    public List<DocumentMetadata> findByProcessedFalse() {
        return metadataStore.values().stream()
                .filter(metadata -> !metadata.isProcessed())
                .toList();
    }

    @Override
    public void save(DocumentMetadata documentMetadata) {
        metadataStore.put(documentMetadata.getDocumentId(), documentMetadata);
    }

    @Override
    public void save(List<DocumentMetadata> metadataList) {
        metadataList.forEach(metadata ->
                metadataStore.put(metadata.getDocumentId(), metadata));
    }

    @Override
    public void updateMetadataProcessedStatus(DocumentId documentId) {
        var metadata = metadataStore.getOrDefault(documentId, null);
        if (metadata == null) return;

        metadata.updateProcessedStatus();
        metadataStore.put(documentId, metadata);
    }
}
