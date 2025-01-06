package com.example.cash_ratio_analyzer_test.repository;

import com.example.cash_ratio_analyzer_test.entity.FinancialData;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class InMemoryEdinetDataRepository implements IEdinetDataRepository {

    private Map<String, FinancialData> financialDataStore = new HashMap<>();

    @Override
    public void save(List<FinancialData> financialDataList) {
        for (var xbrlData : financialDataList) {
            financialDataStore.put(xbrlData.getContextRef(), xbrlData);
        }
    }
}
