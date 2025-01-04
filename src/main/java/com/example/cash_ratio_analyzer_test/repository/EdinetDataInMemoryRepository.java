package com.example.cash_ratio_analyzer_test.repository;

import com.example.cash_ratio_analyzer_test.entity.XbrlData;

import java.util.List;

public class EdinetDataInMemoryRepository implements IEdinetDataRepository {
    @Override
    public void save(List<XbrlData> xbrlDataList) {
        // TODO 一旦インメモリに保存
    }
}
