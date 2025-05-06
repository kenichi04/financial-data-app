package com.example.cash_ratio_analyzer.domain.enums.context;

/**
 * 連結区分を表す列挙型です。
 */
public enum ConsolidatedType {
    /** 連結 */
    CONSOLIDATED("Consolidated"),
    /** 単体 */
    NON_CONSOLIDATED("NonConsolidated");

    private final String label;

    private ConsolidatedType(String label) {
        this.label  = label;
    }

    public String getLabel() {
        return this.label;
    }
}
