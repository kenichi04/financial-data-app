package com.example.financialdataapp.infrastructure.database.jpa.repository;

import com.example.financialdataapp.domain.model.DocumentId;
import com.example.financialdataapp.domain.model.FinancialMetrics;
import com.example.financialdataapp.domain.repository.IFinancialMetricsRepository;
import com.example.financialdataapp.infrastructure.database.jpa.entity.FinancialMetricsEntity;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

@Repository
@Primary
public class JpaFinancialMetricsRepository implements IFinancialMetricsRepository {

    private final FinancialMetricsRepository financialMetricsRepository;

    private final FinancialDocumentRepository financialDocumentRepository;

    public JpaFinancialMetricsRepository(FinancialMetricsRepository financialMetricsRepository, FinancialDocumentRepository financialDocumentRepository) {
        this.financialMetricsRepository = financialMetricsRepository;
        this.financialDocumentRepository = financialDocumentRepository;
    }

    @Override
    public boolean existsByDocumentId(DocumentId documentId) {
        var documentEntity = financialDocumentRepository.findByDocumentId(documentId.toString());
        return documentEntity != null
                && financialMetricsRepository.existsByFinancialDocumentId(documentEntity.getId());
    }

    @Override
    public void save(FinancialMetrics financialMetrics) {
        var documentEntity = financialDocumentRepository.findByDocumentId(financialMetrics.getDocumentId().toString());
        if (documentEntity == null) {
            throw new IllegalArgumentException(
                    "FinancialDocument not found for documentId: " + financialMetrics.getDocumentId());
        }

        // 同一書類の既存行があれば置き換える（financial_document_idはUNIQUE）
        var entity = financialMetricsRepository.findByFinancialDocumentId(documentEntity.getId())
                .orElseGet(FinancialMetricsEntity::new);
        entity.setFinancialDocumentId(documentEntity.getId());
        entity.setEdinetCode(financialMetrics.getEdinetCode().toString());
        entity.setFiscalYearEndDate(financialMetrics.getFiscalYearEndDate());
        entity.setConsolidatedType(financialMetrics.getConsolidatedType());
        entity.setRoe(financialMetrics.getRoe());
        entity.setRoa(financialMetrics.getRoa());
        entity.setOperatingMargin(financialMetrics.getOperatingMargin());
        entity.setEquityRatio(financialMetrics.getEquityRatio());
        entity.setCashRatio(financialMetrics.getCashRatio());

        financialMetricsRepository.save(entity);
    }
}
