package com.example.financialdataapp.presentation.controller;

import com.example.financialdataapp.application.service.FinancialMetricsCalculationUseCase;
import com.example.financialdataapp.application.service.dto.FinancialMetricsCalculationResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/financial-metrics")
public class FinancialMetricsController {

    private final FinancialMetricsCalculationUseCase financialMetricsCalculationUseCase;

    public FinancialMetricsController(FinancialMetricsCalculationUseCase financialMetricsCalculationUseCase) {
        this.financialMetricsCalculationUseCase = financialMetricsCalculationUseCase;
    }

    /**
     * 財務指標が未計算の書類を対象に、指標を一括計算して保存します。
     */
    @PostMapping("/calculate")
    public FinancialMetricsCalculationResult calculate() {
        return financialMetricsCalculationUseCase.calculateAndSaveUncalculatedMetrics();
    }
}
