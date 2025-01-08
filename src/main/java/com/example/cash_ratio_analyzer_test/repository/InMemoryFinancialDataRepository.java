package com.example.cash_ratio_analyzer_test.repository;

import com.example.cash_ratio_analyzer_test.entity.FinancialData;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class InMemoryFinancialDataRepository implements IFinancialDataRepository {

    private Map<Long, FinancialData> financialDataStore = new HashMap<>();
    private Long id = 1L;

    @Override
    public void save(List<FinancialData> financialDataList) {
        for (var financialData : financialDataList) {
            financialDataStore.put(id, financialData);
            id++;
        }
    }
}
