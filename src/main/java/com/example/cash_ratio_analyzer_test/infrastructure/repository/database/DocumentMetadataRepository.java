package com.example.cash_ratio_analyzer_test.infrastructure.repository.database;

import com.example.cash_ratio_analyzer_test.infrastructure.entity.DocumentMetadataEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DocumentMetadataRepository extends JpaRepository<DocumentMetadataEntity, Long> {
    DocumentMetadataEntity findByDocumentId(String string);
    List<DocumentMetadataEntity> findByDocumentIdIn(List<String> documentIds);
    List<DocumentMetadataEntity> findByProcessedFalse();
}
