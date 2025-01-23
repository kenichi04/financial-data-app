package com.example.cash_ratio_analyzer_test.domain.model;

public class Account {
    private String code;  // CashAndDeposits
    private String label; // 現金預金
    // 貸方、借方
    private String balance; // debit

    public Account(String code, String label) {
        this.code = code;
        this.label = label;
    }

    public String getCode() {
        return code;
    }

    public String getLabel() {
        return label;
    }
}
