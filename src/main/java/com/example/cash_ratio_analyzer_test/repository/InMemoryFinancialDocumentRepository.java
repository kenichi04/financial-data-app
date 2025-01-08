package com.example.cash_ratio_analyzer_test.repository;

import com.example.cash_ratio_analyzer_test.entity.FinancialDocument;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class InMemoryFinancialDocumentRepository implements IFinancialDocumentRepository {
    private Map<String, FinancialDocument> financialDocumentStore = new HashMap<>();

    public void save(FinancialDocument financialDocument) {
        financialDocumentStore.put(financialDocument.getDocumentId(), financialDocument);
    }

    public FinancialDocument findByDocumentId(String documentId) {
        return financialDocumentStore.getOrDefault(documentId, null);
    }
}
