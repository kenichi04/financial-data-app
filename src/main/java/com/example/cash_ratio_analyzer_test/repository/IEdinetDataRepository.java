package com.example.cash_ratio_analyzer_test.repository;

import com.example.cash_ratio_analyzer_test.entity.FinancialData;

import java.util.List;

public interface IEdinetDataRepository {
    void save(List<FinancialData> financialDataList);
}
