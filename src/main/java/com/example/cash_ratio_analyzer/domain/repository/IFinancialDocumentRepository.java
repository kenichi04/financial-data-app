package com.example.cash_ratio_analyzer.domain.repository;

import com.example.cash_ratio_analyzer.domain.model.DocumentId;
import com.example.cash_ratio_analyzer.domain.model.FinancialDocument;

public interface IFinancialDocumentRepository {
    FinancialDocument findByDocumentId(DocumentId documentId);

    boolean existsByDocumentId(DocumentId documentId);

    // FinancialDocumentは新規作成のみ想定している
    void create(FinancialDocument financialDocument);
}
