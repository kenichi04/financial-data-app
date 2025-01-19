package com.example.cash_ratio_analyzer_test.presentation.controller.response;

import java.util.List;

public class FinancialDocumentMetadataPostResponse {
    private List<String> documentIds;

    public FinancialDocumentMetadataPostResponse(List<String> documentIds) {
        this.documentIds = documentIds;
    }

    public List<String> getDocumentIds() {
        return documentIds;
    }
}
