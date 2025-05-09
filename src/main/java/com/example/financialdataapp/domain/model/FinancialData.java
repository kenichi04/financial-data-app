package com.example.financialdataapp.domain.model;

import com.example.financialdataapp.domain.enums.DisplayScale;
import com.example.financialdataapp.domain.enums.Currency;
import com.example.financialdataapp.domain.model.context.EdinetContext;

import java.math.BigDecimal;

public class FinancialData {
    private Long id;
    private final Long accountId;
    private final EdinetContext edinetContext;
    private final BigDecimal amount;
    private final DisplayScale displayScale;
    private final Currency currency;

    public FinancialData(Long accountId, EdinetContext edinetContext, BigDecimal amount, DisplayScale displayScale, Currency currency) {
        this.accountId = accountId;
        this.edinetContext = edinetContext;
        this.amount = amount;
        this.displayScale = displayScale;
        this.currency = currency;
    }

    public Long getAccountId() {
        return accountId;
    }

    public EdinetContext getEdinetContext() {
        return edinetContext;
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
