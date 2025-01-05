package com.example.cash_ratio_analyzer_test.repository;

import com.example.cash_ratio_analyzer_test.entity.XbrlData;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EdinetDataInMemoryRepository implements IEdinetDataRepository {

    // テスト確認用のためpublicにしている
    public Map<String, XbrlData> xbrlDataStore = new HashMap<>();
    @Override
    public void save(List<XbrlData> xbrlDataList) {
        for (var xbrlData : xbrlDataList) {
            xbrlDataStore.put(xbrlData.getContextRef(), xbrlData);
        }
    }
}
