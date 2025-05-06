package com.example.cash_ratio_analyzer.domain.enums.context;

/**
 * 会計期間の単位を表す列挙型。
 * <p>
 * INSTANT: 特定の時点を表す。
 * DURATION: 期間を表す。
 * </p>
 */
public enum PeriodUnit {
    /** 時点 */
    INSTANT("Instant"),
    /** 期間 */
    DURATION("Duration");

    private final String label;

    private PeriodUnit(String label) {
        this.label = label;
    }

    public String getName() {
        return this.label;
    }
}
