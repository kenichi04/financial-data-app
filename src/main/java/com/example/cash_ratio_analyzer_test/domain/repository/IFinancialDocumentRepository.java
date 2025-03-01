package com.example.cash_ratio_analyzer_test.domain.repository;

import com.example.cash_ratio_analyzer_test.domain.model.DocumentId;
import com.example.cash_ratio_analyzer_test.domain.model.FinancialDocument;

public interface IFinancialDocumentRepository {
    FinancialDocument findByDocumentId(DocumentId documentId);

    boolean existsByDocumentId(DocumentId documentId);

    // FinancialDocumentは新規作成のみ想定している
    void create(FinancialDocument financialDocument);
}
