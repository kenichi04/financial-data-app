package com.example.cash_ratio_analyzer_test;

public enum EdinetType {
    XBRL(1),
    PDF(2),
    CSV(5);

    private final int id;

    private EdinetType(int id) {
        this.id = id;
    }

    public int code() {
        return this.id;
    }
}
