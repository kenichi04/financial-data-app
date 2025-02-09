package com.example.cash_ratio_analyzer_test.application.service.dto;

import com.example.cash_ratio_analyzer_test.domain.enums.Currency;

import java.util.Map;

public class HeaderInfo {
    private final Map<String, String> deiInfo;
    private final Currency currency;

    public HeaderInfo(Map<String, String> deiInfo, Currency currency) {
        this.deiInfo = deiInfo;
        this.currency = currency;
    }

    public Map<String, String> getDeiInfo() {
        return deiInfo;
    }

    public Currency getCurrency() {
        return currency;
    }
}
