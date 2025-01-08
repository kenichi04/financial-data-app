package com.example.cash_ratio_analyzer_test.service;

import com.example.cash_ratio_analyzer_test.entity.FinancialData;
import com.example.cash_ratio_analyzer_test.repository.IFinancialDataRepository;
import com.example.cash_ratio_analyzer_test.repository.IFinancialDocumentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FinancialDataService {
    private final IFinancialDocumentRepository financialDocumentRepository;

    private final IFinancialDataRepository financialDataRepository;

    public FinancialDataService(IFinancialDocumentRepository financialDocumentRepository, IFinancialDataRepository financialDataRepository) {
        this.financialDocumentRepository = financialDocumentRepository;
        this.financialDataRepository = financialDataRepository;
    }
}
