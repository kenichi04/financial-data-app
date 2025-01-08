package com.example.cash_ratio_analyzer_test.entity;

import java.util.List;

public class FinancialDocument {

    /** 書類管理番号 */
    private String documentId;
    private String documentType;
    private String fiscalYear;
    private List<FinancialData> data;

    public FinancialDocument(String documentId, String documentType, String fiscalYear, List<FinancialData> data) {
        this.documentId = documentId;
        this.documentType = documentType;
        this.fiscalYear = fiscalYear;
        this.data = data;
    }

    public String getDocumentId() {
        return documentId;
    }

    public String getDocumentType() {
        return documentType;
    }

    public String getFiscalYear() {
        return fiscalYear;
    }

    public List<FinancialData> getData() {
        return data;
    }
}
