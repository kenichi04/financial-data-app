package com.example.financialdataapp.application.service;

import com.example.financialdataapp.application.service.dto.FinancialMetricsDto;
import com.example.financialdataapp.application.service.financial.FinancialAccountResolver;
import com.example.financialdataapp.application.service.financial.IFinancialDocumentQueryService;
import com.example.financialdataapp.application.service.financial.ResolvedFinancialAccounts;
import com.example.financialdataapp.domain.model.DocumentId;
import com.example.financialdataapp.domain.model.FinancialMetrics;
import com.example.financialdataapp.domain.service.FinancialMetricsCalculator;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class FinancialMetricsQueryUseCase {

    private final IFinancialDocumentQueryService queryService;
    private final FinancialAccountResolver accountResolver;
    private final FinancialMetricsCalculator calculator;

    public FinancialMetricsQueryUseCase(IFinancialDocumentQueryService queryService,
                                         FinancialAccountResolver accountResolver,
                                         FinancialMetricsCalculator calculator) {
        this.queryService = queryService;
        this.accountResolver = accountResolver;
        this.calculator = calculator;
    }

    public Optional<FinancialMetricsDto> getFinancialMetricsDto(String documentId) {
        return queryService.fetchByFinancialDocumentId(new DocumentId(documentId))
                .map(doc -> {
                    ResolvedFinancialAccounts resolved = accountResolver.resolve(doc.data());
                    FinancialMetrics metrics = calculator.calculate(
                            resolved.netIncome(), resolved.netAssets(), resolved.totalAssets(),
                            resolved.operatingIncome(), resolved.netSales(), resolved.cash());
                    return new FinancialMetricsDto(documentId, metrics.roe(), metrics.roa(),
                            metrics.operatingMargin(), metrics.equityRatio(), metrics.cashRatio());
                });
    }
}
