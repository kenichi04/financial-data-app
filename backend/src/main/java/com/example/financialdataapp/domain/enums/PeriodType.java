package com.example.financialdataapp.domain.enums;

public enum PeriodType {
    /** 時点 */
    INSTANT("instant"),
    /** 期間 */
    DURATION("duration");

    private final String name;

    PeriodType(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }
}
