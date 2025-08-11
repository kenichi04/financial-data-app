package com.example.financialdataapp.domain.repository;

import com.example.financialdataapp.domain.model.DocumentId;
import com.example.financialdataapp.domain.model.DocumentMetadata;

import java.util.List;

public interface IDocumentMetadataRepository {
    DocumentMetadata findByDocumentId(DocumentId documentId);
    List<DocumentMetadata> findByDocumentIds(List<DocumentId> documentIdList);
    List<DocumentMetadata> findByProcessedFalse();
    void save(DocumentMetadata documentMetadata);
    void save(List<DocumentMetadata> metadataList);
    void updateMetadataProcessedStatus(DocumentId documentId);
}
