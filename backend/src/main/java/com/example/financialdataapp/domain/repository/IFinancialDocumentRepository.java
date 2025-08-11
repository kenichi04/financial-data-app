package com.example.financialdataapp.domain.repository;

import com.example.financialdataapp.domain.model.DocumentId;
import com.example.financialdataapp.domain.model.FinancialDocument;

public interface IFinancialDocumentRepository {
    FinancialDocument findByDocumentId(DocumentId documentId);

    boolean existsByDocumentId(DocumentId documentId);

    // FinancialDocumentは新規作成のみ想定している
    void create(FinancialDocument financialDocument);
}
