package com.example.financialdataapp.infrastructure.inmemory;

import com.example.financialdataapp.domain.model.DocumentId;
import com.example.financialdataapp.domain.model.FinancialMetrics;
import com.example.financialdataapp.domain.repository.IFinancialMetricsRepository;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class InMemoryFinancialMetricsRepository implements IFinancialMetricsRepository {
    private Map<DocumentId, FinancialMetrics> financialMetricsStore = new HashMap<>();

    public boolean existsByDocumentId(DocumentId documentId) {
        return financialMetricsStore.containsKey(documentId);
    }

    public void save(FinancialMetrics financialMetrics) {
        financialMetricsStore.put(financialMetrics.getDocumentId(), financialMetrics);
    }

    public FinancialMetrics findByDocumentId(DocumentId documentId) {
        return financialMetricsStore.getOrDefault(documentId, null);
    }
}
