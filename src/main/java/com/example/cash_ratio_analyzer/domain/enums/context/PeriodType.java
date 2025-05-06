package com.example.cash_ratio_analyzer.domain.enums.context;

/**
 * 会計期間の種類を表す列挙型です。
 */
public enum PeriodType {
    /** 当期 */
    CURRENT_YEAR("CurrentYear"),
    /** 前期 */
    PRIOR_1_YEAR("Prior1Year");

    private final String label;

    private PeriodType(String label) {
        this.label = label;
    }

    public String getLabel() {
        return this.label;
    }
}
