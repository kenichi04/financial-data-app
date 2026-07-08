package com.example.financialdataapp.application.service;

import com.example.financialdataapp.application.service.dto.FinancialMetricsCalculationResult;
import com.example.financialdataapp.application.service.metrics.FinancialMetricsCalculationService;
import com.example.financialdataapp.application.service.metrics.IFinancialMetricsQueryService;
import com.example.financialdataapp.domain.model.AccountMaster;
import com.example.financialdataapp.domain.model.DocumentId;
import com.example.financialdataapp.domain.repository.IAccountMasterRepository;
import com.example.financialdataapp.domain.repository.IFinancialDocumentRepository;
import com.example.financialdataapp.domain.repository.IFinancialMetricsRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class FinancialMetricsCalculationUseCase {

    private final IFinancialMetricsQueryService financialMetricsQueryService;
    private final IFinancialDocumentRepository financialDocumentRepository;
    private final IAccountMasterRepository accountMasterRepository;
    private final FinancialMetricsCalculationService financialMetricsCalculationService;
    private final IFinancialMetricsRepository financialMetricsRepository;

    public FinancialMetricsCalculationUseCase(IFinancialMetricsQueryService financialMetricsQueryService, IFinancialDocumentRepository financialDocumentRepository, IAccountMasterRepository accountMasterRepository, FinancialMetricsCalculationService financialMetricsCalculationService, IFinancialMetricsRepository financialMetricsRepository) {
        this.financialMetricsQueryService = financialMetricsQueryService;
        this.financialDocumentRepository = financialDocumentRepository;
        this.accountMasterRepository = accountMasterRepository;
        this.financialMetricsCalculationService = financialMetricsCalculationService;
        this.financialMetricsRepository = financialMetricsRepository;
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
        if (documentIds.isEmpty()) {
            return new FinancialMetricsCalculationResult(0, 0, List.of());
        }

        var accountCodeById = accountMasterRepository.findAll().stream()
                .collect(Collectors.toMap(AccountMaster::getId, AccountMaster::getCode));

        var failedDocumentIds = new ArrayList<String>();
        var savedCount = 0;
        for (var documentId : documentIds) {
            try {
                calculateAndSave(documentId, accountCodeById);
                savedCount++;
            } catch (RuntimeException e) {
                failedDocumentIds.add(documentId.toString());
            }
        }
        return new FinancialMetricsCalculationResult(documentIds.size(), savedCount, failedDocumentIds);
    }

    private void calculateAndSave(DocumentId documentId, Map<Long, String> accountCodeById) {
        var document = financialDocumentRepository.findByDocumentId(documentId);
        if (document == null) {
            throw new IllegalStateException("FinancialDocument not found: " + documentId);
        }
        var metrics = financialMetricsCalculationService.calculate(document, accountCodeById);
        financialMetricsRepository.save(metrics);
    }
}
