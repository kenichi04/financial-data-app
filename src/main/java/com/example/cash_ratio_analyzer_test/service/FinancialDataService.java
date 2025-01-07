package com.example.cash_ratio_analyzer_test.service;

import com.example.cash_ratio_analyzer_test.entity.FinancialData;
import com.example.cash_ratio_analyzer_test.repository.IFinancialDataRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FinancialDataService {

    private final IFinancialDataRepository financialDataRepository;

    public FinancialDataService(IFinancialDataRepository financialDataRepository) {
        this.financialDataRepository = financialDataRepository;
    }

    public List<FinancialData> financialDataList() {
        return financialDataRepository.findAll();
    }
}
