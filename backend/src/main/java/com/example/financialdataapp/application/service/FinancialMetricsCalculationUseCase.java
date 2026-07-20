package com.example.financialdataapp.application.service;

import com.example.financialdataapp.application.service.dto.FinancialMetricsCalculationResult;
import com.example.financialdataapp.application.service.metrics.FinancialMetricsService;
import com.example.financialdataapp.application.service.metrics.IFinancialMetricsQueryService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class FinancialMetricsCalculationUseCase {

    private final IFinancialMetricsQueryService financialMetricsQueryService;

    private final FinancialMetricsService financialMetricsService;

    public FinancialMetricsCalculationUseCase(IFinancialMetricsQueryService financialMetricsQueryService, FinancialMetricsService financialMetricsService) {
        this.financialMetricsQueryService = financialMetricsQueryService;
        this.financialMetricsService = financialMetricsService;
    }

    /**
     * 財務指標が未計算の書類を抽出し、指標を計算して保存します。
     *
     * <p>1書類の失敗でバッチ全体を止めないよう、失敗した書類はスキップして
     * 結果に書類IDを記録します。</p>
     *
     * @return バッチ計算の実行結果
     */
    public FinancialMetricsCalculationResult calculateAndSaveUncalculatedMetrics() {
        var documentIds = financialMetricsQueryService.findDocumentIdsWithoutMetrics();

        var failedDocumentIds = new ArrayList<String>();
        var savedCount = 0;
        for (var documentId : documentIds) {
            try {
                financialMetricsService.calculateAndSaveMetrics(documentId);
                savedCount++;
            } catch (RuntimeException e) {
                failedDocumentIds.add(documentId.toString());
            }
        }
        return new FinancialMetricsCalculationResult(documentIds.size(), savedCount, failedDocumentIds);
    }
}
