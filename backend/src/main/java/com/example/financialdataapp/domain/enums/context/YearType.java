package com.example.financialdataapp.domain.enums.context;

/**
 * 会計期間の種類を表す列挙型です。
 */
public enum YearType {
    /** 当期 */
    CURRENT_YEAR("CurrentYear"),
    /** 前期 */
    PRIOR_1_YEAR("Prior1Year");

    private final String label;

    private YearType(String label) {
        this.label = label;
    }

    public String getLabel() {
        return this.label;
    }
}
