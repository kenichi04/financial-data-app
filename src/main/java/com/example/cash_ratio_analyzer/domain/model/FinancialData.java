package com.example.cash_ratio_analyzer.domain.model;

import com.example.cash_ratio_analyzer.domain.enums.DisplayScale;
import com.example.cash_ratio_analyzer.domain.enums.Currency;

import java.math.BigDecimal;

public class FinancialData {
    private Long id;
    // TODO 必要かどうか検討
    private DocumentId documentId;
    private final AccountMaster accountMaster;
    // 前期、今期の判定
    private final String periodContext;
    private final BigDecimal amount;
    private final DisplayScale displayScale;
    private final Currency currency;

    public FinancialData(AccountMaster accountMaster, String periodContext, BigDecimal amount, DisplayScale displayScale, Currency currency) {
        this.accountMaster = accountMaster;
        this.periodContext = periodContext;
        this.amount = amount;
        this.displayScale = displayScale;
        this.currency = currency;
    }

    public AccountMaster getAccount() {
        return accountMaster;
    }

    public String getPeriodContext() {
        return periodContext;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public DisplayScale getDisplayScale() {
        return displayScale;
    }

    public Currency getCurrency() {
        return currency;
    }
}
