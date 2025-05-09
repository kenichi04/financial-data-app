package com.example.financialdataapp.infrastructure.database.repository;

import com.example.financialdataapp.infrastructure.database.entity.FinancialDocumentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FinancialDocumentRepository extends JpaRepository<FinancialDocumentEntity, Long> {
    FinancialDocumentEntity findByDocumentId(String documentId);
    boolean existsByDocumentId(String string);
}
