package com.example.cash_ratio_analyzer_test.entity;

import java.util.List;

public class FinancialDocument {

    /** 書類管理番号 */
    private String documentId;
//    private String documentType;
//    private String fiscalYear;
    private List<FinancialData> data;

    public FinancialDocument(String documentId) {
        this.documentId = documentId;
    }

    public String getDocumentId() {
        return documentId;
    }

    public List<FinancialData> getData() {
        return data;
    }
}
