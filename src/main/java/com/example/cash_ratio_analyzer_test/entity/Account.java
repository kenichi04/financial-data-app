package com.example.cash_ratio_analyzer_test.entity;

public class Account {
    private String accountCode;
    private String name;

    public Account(String accountCode, String name) {
        this.accountCode = accountCode;
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
