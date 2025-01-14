package com.example.cash_ratio_analyzer_test.domain.model;

import com.example.cash_ratio_analyzer_test.domain.enums.EdinetDocumentType;

import java.time.LocalDate;

public class FinancialDocumentMetadata {

    private String documentId;

    private String description;

    /** 提出者EDINETコード */
    private String edinetCode;

    /** 提出者名 */
    private String filerName;

    private EdinetDocumentType documentType;

    /** 提出日 */
    private LocalDate submissionDate;

    /** 書類取得APIで取得済か */
    private boolean processed = false;

    public FinancialDocumentMetadata(String documentId, String description, String edinetCode, String filerName, EdinetDocumentType documentType, LocalDate submissionDate) {
        this.documentId = documentId;
        this.description = description;
        this.edinetCode = edinetCode;
        this.filerName = filerName;
        this.documentType = documentType;
        this.submissionDate = submissionDate;
        this.processed = false;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void updateProcessedStatus() {
        this.processed = true;
    }
}
