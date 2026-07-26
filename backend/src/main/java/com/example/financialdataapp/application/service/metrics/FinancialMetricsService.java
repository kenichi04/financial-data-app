package com.example.financialdataapp.application.service.metrics;

import com.example.financialdataapp.domain.model.DocumentId;
import com.example.financialdataapp.domain.repository.IFinancialDocumentRepository;
import com.example.financialdataapp.domain.repository.IFinancialMetricsRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
public class FinancialMetricsService {

    private final IFinancialDocumentRepository financialDocumentRepository;

    private final FinancialMetricsCalculationService financialMetricsCalculationService;

    private final IFinancialMetricsRepository financialMetricsRepository;

    public FinancialMetricsService(IFinancialDocumentRepository financialDocumentRepository, FinancialMetricsCalculationService financialMetricsCalculationService, IFinancialMetricsRepository financialMetricsRepository) {
        this.financialDocumentRepository = financialDocumentRepository;
        this.financialMetricsCalculationService = financialMetricsCalculationService;
        this.financialMetricsRepository = financialMetricsRepository;
    }

    /**
     * 指定された書類IDの財務書類から財務指標を計算して保存します。
     *
     * <p>同一書類の指標が保存済みの場合は置き換えます。</p>
     *
     * @param documentId 計算対象の書類ID
     * @param accountCodeById 勘定科目ID→コードのマップ（バッチ全体で共有）
     * @throws IllegalStateException 書類IDに対応する財務書類が存在しない場合
     */
    @Transactional
    public void calculateAndSaveMetrics(DocumentId documentId, Map<Long, String> accountCodeById) {
        var document = financialDocumentRepository.findByDocumentId(documentId);
        if (document == null) {
            throw new IllegalStateException("FinancialDocument not found: " + documentId);
        }

        var metrics = financialMetricsCalculationService.calculate(document, accountCodeById);
        financialMetricsRepository.save(metrics);
    }
}
