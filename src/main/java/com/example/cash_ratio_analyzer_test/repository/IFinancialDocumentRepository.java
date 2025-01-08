package com.example.cash_ratio_analyzer_test.repository;

import com.example.cash_ratio_analyzer_test.entity.FinancialDocument;

public interface IFinancialDocumentRepository {
    FinancialDocument findByDocumentId(String documentId);
    void save(FinancialDocument financialDocument);
}
