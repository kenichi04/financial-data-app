package com.example.cash_ratio_analyzer.domain.model;

import com.example.cash_ratio_analyzer.domain.enums.DisplayScale;
import com.example.cash_ratio_analyzer.domain.enums.Currency;

import java.math.BigDecimal;

public class FinancialData {
    private Long id;
    // TODO 必要かどうか検討
    private DocumentId documentId;
    // TODO 集約外のため、インスタンスではなくaccount_idを持つ
    private final AccountMaster accountMaster;
    private final String contextId;
    private final BigDecimal amount;
    private final DisplayScale displayScale;
    private final Currency currency;

    public FinancialData(AccountMaster accountMaster, String contextId, BigDecimal amount, DisplayScale displayScale, Currency currency) {
        this.accountMaster = accountMaster;
        this.contextId = contextId;
        this.amount = amount;
        this.displayScale = displayScale;
        this.currency = currency;
    }

    public AccountMaster getAccount() {
        return accountMaster;
    }

    public String getContextId() {
        return contextId;
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
