package com.example.cash_ratio_analyzer_test.presentation.controller.response;

public class FinancialDocumentPostResponse {
    private String documentId;

    public FinancialDocumentPostResponse(String documentId) {
        this.documentId = documentId;
    }

    public String getDocumentId() {
        return documentId;
    }
}
