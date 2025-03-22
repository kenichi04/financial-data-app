package com.example.cash_ratio_analyzer.domain.repository;

import com.example.cash_ratio_analyzer.domain.model.DocumentId;
import com.example.cash_ratio_analyzer.domain.model.DocumentMetadata;

import java.util.List;

public interface IDocumentMetadataRepository {
    DocumentMetadata findByDocumentId(DocumentId documentId);
    List<DocumentMetadata> findByDocumentIds(List<DocumentId> documentIdList);
    List<DocumentMetadata> findByProcessedFalse();
    void save(DocumentMetadata documentMetadata);
    void save(List<DocumentMetadata> metadataList);
    void updateMetadataProcessedStatus(DocumentId documentId);
}
