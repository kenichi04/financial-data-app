package com.example.financialdataapp.presentation.controller.response;

public class FinancialDocumentPostResponse {
    private final String documentId;

    public FinancialDocumentPostResponse(String documentId) {
        this.documentId = documentId;
    }

    public String getDocumentId() {
        return documentId;
    }
}
