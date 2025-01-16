package com.example.cash_ratio_analyzer_test.domain.model;

import java.util.List;

public class FinancialDocument {

    /** 書類管理番号 */
    private DocumentId documentId;
//    private String documentType;
//    private String fiscalYear;
    private List<FinancialData> data;

    public FinancialDocument(DocumentId documentId) {
        this.documentId = documentId;
    }

    public DocumentId getDocumentId() {
        return documentId;
    }

    public void createData(List<FinancialData> data) {
        this.data = data;
    }
}
