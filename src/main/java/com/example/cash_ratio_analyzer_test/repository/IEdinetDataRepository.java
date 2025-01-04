package com.example.cash_ratio_analyzer_test.repository;

import com.example.cash_ratio_analyzer_test.entity.XbrlData;

import java.util.List;

public interface IEdinetDataRepository {
    void save(List<XbrlData> xbrlDataList);
}
