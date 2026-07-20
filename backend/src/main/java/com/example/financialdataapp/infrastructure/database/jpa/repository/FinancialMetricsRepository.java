package com.example.financialdataapp.infrastructure.database.jpa.repository;

import com.example.financialdataapp.infrastructure.database.jpa.entity.FinancialMetricsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FinancialMetricsRepository extends JpaRepository<FinancialMetricsEntity, Long> {
    Optional<FinancialMetricsEntity> findByFinancialDocumentId(Long financialDocumentId);
    boolean existsByFinancialDocumentId(Long financialDocumentId);
}
