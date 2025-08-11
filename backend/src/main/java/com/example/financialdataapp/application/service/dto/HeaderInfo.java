package com.example.financialdataapp.application.service.dto;

import com.example.financialdataapp.domain.enums.Currency;

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
