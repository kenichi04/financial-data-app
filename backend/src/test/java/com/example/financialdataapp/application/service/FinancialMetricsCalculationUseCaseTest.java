package com.example.financialdataapp.application.service;

import com.example.financialdataapp.application.service.metrics.FinancialMetricsCalculationService;
import com.example.financialdataapp.application.service.metrics.IFinancialMetricsQueryService;
import com.example.financialdataapp.domain.enums.Balance;
import com.example.financialdataapp.domain.enums.Currency;
import com.example.financialdataapp.domain.enums.DisplayScale;
import com.example.financialdataapp.domain.enums.PeriodType;
import com.example.financialdataapp.domain.model.AccountMaster;
import com.example.financialdataapp.domain.model.DocumentId;
import com.example.financialdataapp.domain.model.EdinetCode;
import com.example.financialdataapp.domain.model.FinancialData;
import com.example.financialdataapp.domain.model.FinancialDocument;
import com.example.financialdataapp.domain.model.context.EdinetContext;
import com.example.financialdataapp.infrastructure.inmemory.InMemoryFinancialDocumentRepository;
import com.example.financialdataapp.infrastructure.inmemory.InMemoryFinancialMetricsRepository;
import com.example.financialdataapp.domain.repository.IAccountMasterRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FinancialMetricsCalculationUseCaseTest {

    private static final Long PROFIT_LOSS_ID = 1L;
    private static final Long ASSETS_ID = 2L;
    private static final Long SHAREHOLDERS_EQUITY_ID = 3L;

    @Mock
    private IFinancialMetricsQueryService financialMetricsQueryService;

    @Mock
    private IAccountMasterRepository accountMasterRepository;

    private InMemoryFinancialDocumentRepository financialDocumentRepository;

    private InMemoryFinancialMetricsRepository financialMetricsRepository;

    private FinancialMetricsCalculationUseCase useCase;

    @BeforeEach
    void setUp() {
        financialDocumentRepository = new InMemoryFinancialDocumentRepository();
        financialMetricsRepository = new InMemoryFinancialMetricsRepository();
        useCase = new FinancialMetricsCalculationUseCase(
                financialMetricsQueryService,
                financialDocumentRepository,
                accountMasterRepository,
                new FinancialMetricsCalculationService(),
                financialMetricsRepository);
    }

    @Test
    void calculateAndSaveUncalculatedMetrics_ShouldCalculateAndSaveMetrics_ForAllUncalculatedDocuments() {
        var documentId1 = new DocumentId("TEST0001");
        var documentId2 = new DocumentId("TEST0002");
        financialDocumentRepository.create(createDocument(documentId1, "100", "1000"));
        financialDocumentRepository.create(createDocument(documentId2, "300", "1000"));
        when(financialMetricsQueryService.findDocumentIdsWithoutMetrics())
                .thenReturn(List.of(documentId1, documentId2));
        when(accountMasterRepository.findAll()).thenReturn(createAccountMasters());

        var result = useCase.calculateAndSaveUncalculatedMetrics();

        assertEquals(2, result.targetCount());
        assertEquals(2, result.savedCount());
        assertTrue(result.failedDocumentIds().isEmpty());

        var metrics1 = financialMetricsRepository.findByDocumentId(documentId1);
        assertNotNull(metrics1);
        // ROA = 100 / 1000 × 100
        assertEquals(new BigDecimal("10.0000"), metrics1.getRoa());
        var metrics2 = financialMetricsRepository.findByDocumentId(documentId2);
        assertNotNull(metrics2);
        // ROA = 300 / 1000 × 100
        assertEquals(new BigDecimal("30.0000"), metrics2.getRoa());
    }

    @Test
    void calculateAndSaveUncalculatedMetrics_ShouldReturnEmptyResult_WhenNoUncalculatedDocuments() {
        when(financialMetricsQueryService.findDocumentIdsWithoutMetrics()).thenReturn(List.of());

        var result = useCase.calculateAndSaveUncalculatedMetrics();

        assertEquals(0, result.targetCount());
        assertEquals(0, result.savedCount());
        assertTrue(result.failedDocumentIds().isEmpty());
        verifyNoInteractions(accountMasterRepository);
    }

    @Test
    void calculateAndSaveUncalculatedMetrics_ShouldSkipAndRecordFailure_WhenDocumentNotFound() {
        var existingId = new DocumentId("TEST0001");
        var missingId = new DocumentId("TEST9999");
        financialDocumentRepository.create(createDocument(existingId, "100", "1000"));
        when(financialMetricsQueryService.findDocumentIdsWithoutMetrics())
                .thenReturn(List.of(missingId, existingId));
        when(accountMasterRepository.findAll()).thenReturn(createAccountMasters());

        var result = useCase.calculateAndSaveUncalculatedMetrics();

        // 失敗した書類はスキップし、残りの書類の計算は継続すること
        assertEquals(2, result.targetCount());
        assertEquals(1, result.savedCount());
        assertEquals(List.of("TEST9999"), result.failedDocumentIds());
        assertNotNull(financialMetricsRepository.findByDocumentId(existingId));
        assertNull(financialMetricsRepository.findByDocumentId(missingId));
    }

    private List<AccountMaster> createAccountMasters() {
        return List.of(
                new AccountMaster(PROFIT_LOSS_ID, "ProfitLoss", "当期純利益又は当期純損失（△）", "Profit (loss)", PeriodType.DURATION, Balance.CREDIT, 1),
                new AccountMaster(ASSETS_ID, "Assets", "資産", "Assets", PeriodType.INSTANT, Balance.DEBIT, 1),
                new AccountMaster(SHAREHOLDERS_EQUITY_ID, "ShareholdersEquity", "株主資本", "Shareholders' equity", PeriodType.INSTANT, Balance.CREDIT, 1));
    }

    private FinancialDocument createDocument(DocumentId documentId, String profitLoss, String assets) {
        return new FinancialDocument(
                documentId,
                new EdinetCode("TEST01"),
                "テスト様式",
                LocalDate.of(2026, 3, 31),
                Currency.JPY,
                List.of(
                        data(PROFIT_LOSS_ID, profitLoss, "CurrentYearDuration"),
                        data(ASSETS_ID, assets, "CurrentYearInstant"),
                        data(SHAREHOLDERS_EQUITY_ID, "400", "CurrentYearInstant")));
    }

    private FinancialData data(Long accountId, String amount, String contextRef) {
        return new FinancialData(
                accountId,
                EdinetContext.fromContextRef(contextRef),
                new BigDecimal(amount),
                DisplayScale.YEN,
                Currency.JPY);
    }
}
