package com.example.financialdataapp.application.service;

import com.example.financialdataapp.application.service.dto.FinancialMetricsCalculationResult;
import com.example.financialdataapp.application.service.metrics.FinancialMetricsService;
import com.example.financialdataapp.application.service.metrics.IFinancialMetricsQueryService;
import com.example.financialdataapp.domain.model.AccountMaster;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class FinancialMetricsCalculationUseCase {

    private final IFinancialMetricsQueryService financialMetricsQueryService;

    private final FinancialMetricsService financialMetricsService;

    private final AccountService accountService;

    public FinancialMetricsCalculationUseCase(IFinancialMetricsQueryService financialMetricsQueryService, FinancialMetricsService financialMetricsService, AccountService accountService) {
        this.financialMetricsQueryService = financialMetricsQueryService;
        this.financialMetricsService = financialMetricsService;
        this.accountService = accountService;
    }

    /**
     * 財務指標が未計算の書類を抽出し、指標を計算して保存します。
     *
     * <p>1書類の失敗でバッチ全体を止めないよう、失敗した書類はスキップして
     * 結果に書類IDを記録します。</p>
     *
     * <p>勘定科目マスタはバッチ全体で不変のため、ループ前に1回だけロードして
     * 各書類の計算に使い回す。</p>
     *
     * @return バッチ計算の実行結果
     */
    public FinancialMetricsCalculationResult calculateAndSaveUncalculatedMetrics() {
        var documentIds = financialMetricsQueryService.findDocumentIdsWithoutMetrics();
        if (documentIds.isEmpty()) {
            return new FinancialMetricsCalculationResult(0, 0, List.of());
        }

        var accountCodeById = accountService.getAccounts().stream()
                .collect(Collectors.toMap(
                        account -> Objects.requireNonNull(
                                account.getId(),
                                "AccountMaster.id must not be null when calculating financial metrics"),
                        AccountMaster::getCode));

        var failedDocumentIds = new ArrayList<String>();
        var savedCount = 0;
        for (var documentId : documentIds) {
            try {
                financialMetricsService.calculateAndSaveMetrics(documentId, accountCodeById);
                savedCount++;
            } catch (RuntimeException e) {
                failedDocumentIds.add(documentId.toString());
            }
        }
        return new FinancialMetricsCalculationResult(documentIds.size(), savedCount, failedDocumentIds);
    }
}
