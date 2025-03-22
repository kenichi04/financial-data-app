package com.example.cash_ratio_analyzer.domain.model;

import com.example.cash_ratio_analyzer.domain.enums.Balance;

public class AccountMaster {
    private Long id;
    private String code;  // CashAndDeposits
    private String nameJp; // 現金預金
    private String nameEn;
    // 貸借区分（借方/貸方）
    private Balance balance;

    public AccountMaster(String code, String nameJp, String nameEn, Balance balance) {
        this.code = code;
        this.nameJp = nameJp;
        this.nameEn = nameEn;
        this.balance = balance;
    }

    // TODO 要確認（AccountMasterはマスタデータとして手動INSERTのため、モデルクラス作成時はIDが存在する想定）
    public AccountMaster(Long id, String code, String nameJp, String nameEn, Balance balance) {
        this.id = id;
        this.code = code;
        this.nameJp = nameJp;
        this.nameEn = nameEn;
        this.balance = balance;
    }

    public Long getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public String getNameJp() {
        return nameJp;
    }

    public String getNameEn() {
        return nameEn;
    }

    public Balance getBalance() {
        return balance;
    }
}
