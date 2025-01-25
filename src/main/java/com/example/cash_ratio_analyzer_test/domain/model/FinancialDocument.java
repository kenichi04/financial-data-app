package com.example.cash_ratio_analyzer_test.domain.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FinancialDocument {
    private Long id;

    /** 書類管理番号 */
    private final DocumentId documentId;
//    private String documentType;
//    private String fiscalYear;
    private final List<FinancialData> data;

    public FinancialDocument(DocumentId documentId, List<FinancialData> data) {
        if (documentId == null) {
            throw new IllegalArgumentException("DocumentId cannot be null");
        }
        if (data == null || data.isEmpty()) {
            throw new IllegalArgumentException("FinancialData list cannot be null or empty");
        }
        this.documentId = documentId;
        this.data = new ArrayList<>(data);
    }

    public DocumentId getDocumentId() {
        return documentId;
    }

    public List<FinancialData> getData() {
        // リストの直接変更を防ぐため、コピーを返す
        return Collections.unmodifiableList(data);
    }
}