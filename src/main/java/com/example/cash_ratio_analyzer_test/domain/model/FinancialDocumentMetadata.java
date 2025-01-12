package com.example.cash_ratio_analyzer_test.domain.model;

import java.time.LocalDate;

public class FinancialDocumentMetadata {

    private String documentId;

    private String companyId;

    private String documentType;

    /** 提出日 */
    private LocalDate submissionDate;

    /** 書類取得APIで取得済か */
    private boolean isFetched;
}
