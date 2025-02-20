package com.example.cash_ratio_analyzer_test.domain.model;

import com.example.cash_ratio_analyzer_test.domain.enums.Currency;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FinancialDocument {
    private Long id;

    /** 書類管理番号 */
    private final DocumentId documentId;

    /** 提出者EDINETコード */
    private final EdinetCode edinetCode;

    /** 様式 */
    private final String documentType;

    /** 会計年度 */
    private final LocalDate fiscalYearEndDate;

    private final Currency currency;

    private final List<FinancialData> data;

    public FinancialDocument(DocumentId documentId, EdinetCode edinetCode, String documentType, LocalDate fiscalYearEndDate, Currency currency, List<FinancialData> data) {
        if (documentId == null) {
            throw new IllegalArgumentException("DocumentId cannot be null");
        }
        if (data == null || data.isEmpty()) {
            throw new IllegalArgumentException("FinancialData list cannot be null or empty");
        }
        this.documentId = documentId;
        this.edinetCode = edinetCode;
        this.documentType = documentType;
        this.fiscalYearEndDate = fiscalYearEndDate;
        this.currency = currency;
        this.data = new ArrayList<>(data);
    }

    public DocumentId getDocumentId() {
        return documentId;
    }

    public EdinetCode getEdinetCode() {
        return edinetCode;
    }

    public String getDocumentType() {
        return documentType;
    }

    public LocalDate getFiscalYearEndDate() {
        return fiscalYearEndDate;
    }

    public Currency getCurrency() {
        return currency;
    }

    public List<FinancialData> getData() {
        // リストの直接変更を防ぐため、コピーを返す
        return Collections.unmodifiableList(data);
    }
}