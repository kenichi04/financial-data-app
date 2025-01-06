package com.example.cash_ratio_analyzer_test.repository;

import com.example.cash_ratio_analyzer_test.entity.XbrlData;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class InMemoryEdinetDataRepository implements IEdinetDataRepository {

    private Map<String, XbrlData> xbrlDataStore = new HashMap<>();

    @Override
    public void save(List<XbrlData> xbrlDataList) {
        for (var xbrlData : xbrlDataList) {
            xbrlDataStore.put(xbrlData.getContextRef(), xbrlData);
        }
    }
}
