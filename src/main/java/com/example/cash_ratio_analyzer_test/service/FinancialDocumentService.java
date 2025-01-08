package com.example.cash_ratio_analyzer_test.service;

import com.example.cash_ratio_analyzer_test.entity.FinancialData;
import com.example.cash_ratio_analyzer_test.entity.FinancialDocument;
import com.example.cash_ratio_analyzer_test.repository.IFinancialDocumentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FinancialDocumentService {
    private final IFinancialDocumentRepository financialDocumentRepository;

    public FinancialDocumentService(IFinancialDocumentRepository financialDocumentRepository, IFinancialDocumentRepository financialDocumentRepository1) {
        this.financialDocumentRepository = financialDocumentRepository1;
    }

    public FinancialDocument getFinancialDocument(String documentId) {
        return financialDocumentRepository.findByDocumentId(documentId);
    }

    // TODO transactionalアノテーションを付与する
    public void saveFinancialData(String documentId, List<FinancialData> financialDataList) {
        var financialDocument = new FinancialDocument(documentId);
        financialDocument.getData().addAll(financialDataList);
        financialDocumentRepository.save(financialDocument);
    }
}
