package com.example.cash_ratio_analyzer_test.infrastructure.repository.database;

import com.example.cash_ratio_analyzer_test.infrastructure.entity.FinancialDocumentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FinancialDocumentRepository extends JpaRepository<FinancialDocumentEntity, Long> {
    FinancialDocumentEntity findByDocumentId(String documentId);
}
