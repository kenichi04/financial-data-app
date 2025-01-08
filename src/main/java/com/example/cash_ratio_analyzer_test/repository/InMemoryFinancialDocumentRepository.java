package com.example.cash_ratio_analyzer_test.repository;

import com.example.cash_ratio_analyzer_test.entity.FinancialDocument;
import org.springframework.stereotype.Repository;

@Repository
public class InMemoryFinancialDocumentRepository implements IFinancialDocumentRepository {
    public void save(FinancialDocument financialDocument) {
        // Save the financial document to the database
    }

    public FinancialDocument findByDocumentId(String documentId) {
        // Find the financial document by the document ID
        return null;
    }
}
