package com.example.financialdataapp.application.service.metrics;

import com.example.financialdataapp.domain.model.AccountMaster;
import com.example.financialdataapp.domain.model.DocumentId;
import com.example.financialdataapp.domain.repository.IAccountMasterRepository;
import com.example.financialdataapp.domain.repository.IFinancialDocumentRepository;
import com.example.financialdataapp.domain.repository.IFinancialMetricsRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

@Service
public class FinancialMetricsService {

    private final IFinancialDocumentRepository financialDocumentRepository;

    private final IAccountMasterRepository accountMasterRepository;

    private final FinancialMetricsCalculationService financialMetricsCalculationService;

    private final IFinancialMetricsRepository financialMetricsRepository;

    public FinancialMetricsService(IFinancialDocumentRepository financialDocumentRepository, IAccountMasterRepository accountMasterRepository, FinancialMetricsCalculationService financialMetricsCalculationService, IFinancialMetricsRepository financialMetricsRepository) {
        this.financialDocumentRepository = financialDocumentRepository;
        this.accountMasterRepository = accountMasterRepository;
        this.financialMetricsCalculationService = financialMetricsCalculationService;
        this.financialMetricsRepository = financialMetricsRepository;
    }

    /**
     * 指定された書類IDの財務書類から財務指標を計算して保存します。
     *
     * <p>同一書類の指標が保存済みの場合は置き換えます。</p>
     *
     * @param documentId 計算対象の書類ID
     * @throws IllegalStateException 書類IDに対応する財務書類が存在しない場合
     */
    @Transactional
    public void calculateAndSaveMetrics(DocumentId documentId) {
        var document = financialDocumentRepository.findByDocumentId(documentId);
        if (document == null) {
            throw new IllegalStateException("FinancialDocument not found: " + documentId);
        }

        var accountCodeById = accountMasterRepository.findAll().stream()
                .collect(Collectors.toMap(AccountMaster::getId, AccountMaster::getCode));

        var metrics = financialMetricsCalculationService.calculate(document, accountCodeById);
        financialMetricsRepository.save(metrics);
    }
}
