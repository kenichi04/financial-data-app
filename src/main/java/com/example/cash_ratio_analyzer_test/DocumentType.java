package com.example.cash_ratio_analyzer_test;

public enum DocumentType {
    XBRL(1),
    PDF(2),
    CSV(5);

    private final int id;

    private DocumentType(int id) {
        this.id = id;
    }

    public int code() {
        return this.id;
    }
}
