package com.example.financialdataapp.domain.model;

import com.example.financialdataapp.domain.enums.Balance;
import com.example.financialdataapp.domain.enums.PeriodType;

public class AccountMaster {
    private Long id;
    private String code;  // CashAndDeposits
    private String nameJp; // 現金預金
    private String nameEn;
    private PeriodType periodType;
    private Balance balance;
    private Integer depth;

    public AccountMaster(String code, String nameJp, String nameEn, PeriodType periodType, Balance balance, Integer depth) {
        this.code = code;
        this.nameJp = nameJp;
        this.nameEn = nameEn;
        this.periodType = periodType;
        this.balance = balance;
        this.depth = depth;
    }

    // TODO 要確認（AccountMasterはマスタデータとして手動INSERTのため、モデルクラス作成時はIDが存在する想定）
    public AccountMaster(Long id, String code, String nameJp, String nameEn, PeriodType periodType, Balance balance, Integer depth) {
        this.id = id;
        this.code = code;
        this.nameJp = nameJp;
        this.nameEn = nameEn;
        this.periodType = periodType;
        this.balance = balance;
        this.depth = depth;
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

    public PeriodType getPeriodType() {
        return periodType;
    }

    public Balance getBalance() {
        return balance;
    }

    public Integer getDepth() {
        return depth;
    }
}
