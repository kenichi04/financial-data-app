package com.example.financialdataapp.domain.repository;

import com.example.financialdataapp.domain.model.DocumentId;
import com.example.financialdataapp.domain.model.FinancialMetrics;

public interface IFinancialMetricsRepository {
    boolean existsByDocumentId(DocumentId documentId);

    // 再計算時は同一書類の既存行を置き換える
    void save(FinancialMetrics financialMetrics);
}
