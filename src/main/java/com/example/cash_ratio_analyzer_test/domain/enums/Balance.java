package com.example.cash_ratio_analyzer_test.domain.enums;

/**
 * 貸借区分を表す列挙型です。
 */
public enum Balance {
    /** 借方 */
    DEBIT("debit"),
    /** 貸方 */
    CREDIT("credit"),
    NONE("none");

    private final String name;

    private Balance(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }
}
