package com.example.cash_ratio_analyzer_test.domain.model;

import com.example.cash_ratio_analyzer_test.domain.enums.Balance;

public class Account {
    private Long id;
    private String code;  // CashAndDeposits
    private String nameJp; // 現金預金
    private String nameEn;
    // 貸借区分（借方/貸方）
    private Balance balance;

    public Account(String code, String nameJp, String nameEn, Balance balance) {
        this.code = code;
        this.nameJp = nameJp;
        this.nameEn = nameEn;
        this.balance = balance;
    }

    public String getCode() {
        return code;
    }

    public String getNameJp() {
        return nameJp;
    }
}
