package com.example.cash_ratio_analyzer_test.infrastructure.repository;

import com.example.cash_ratio_analyzer_test.domain.model.DocumentId;
import com.example.cash_ratio_analyzer_test.domain.model.FinancialDocument;
import com.example.cash_ratio_analyzer_test.domain.repository.IFinancialDocumentRepository;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class InMemoryFinancialDocumentRepository implements IFinancialDocumentRepository {
    private Map<DocumentId, FinancialDocument> financialDocumentStore = new HashMap<>();

    public void save(FinancialDocument financialDocument) {
        financialDocumentStore.put(financialDocument.getDocumentId(), financialDocument);
    }

    public FinancialDocument findByDocumentId(DocumentId documentId) {
        return financialDocumentStore.getOrDefault(documentId, null);
    }
}
