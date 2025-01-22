package com.example.cash_ratio_analyzer_test.domain.model;

public class Account {
    private String accountCode;
    private String name;
    // TODO 貸方、借方の判定は？属する種類（貸借対照表、損益計算書）は？

    public Account(String accountCode, String name) {
        this.accountCode = accountCode;
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
