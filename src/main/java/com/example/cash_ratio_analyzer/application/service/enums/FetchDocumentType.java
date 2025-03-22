package com.example.cash_ratio_analyzer.application.service.enums;

/**
 * EDINET書類取得APIで取得する書類の種類を表す列挙型.
 */
public enum FetchDocumentType {
    XBRL(1),
    PDF(2),
    CSV(5);

    private final int id;

    private FetchDocumentType(int id) {
        this.id = id;
    }

    public int code() {
        return this.id;
    }
}
