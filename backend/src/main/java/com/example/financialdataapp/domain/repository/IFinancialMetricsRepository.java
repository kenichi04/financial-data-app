package com.example.financialdataapp.domain.repository;

import com.example.financialdataapp.domain.model.FinancialMetrics;

public interface IFinancialMetricsRepository {
    // 再計算時は同一書類の既存行を置き換える
    void save(FinancialMetrics financialMetrics);
}
