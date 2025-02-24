package com.example.cash_ratio_analyzer_test.infrastructure.repository.database;

import com.example.cash_ratio_analyzer_test.infrastructure.entity.FinancialDocumentMetadataEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FinancialDocumentMetadataRepository extends JpaRepository<FinancialDocumentMetadataEntity, Long> {
    FinancialDocumentMetadataEntity findByDocumentId(String string);

    List<FinancialDocumentMetadataEntity> findByProcessedFalse();
}
