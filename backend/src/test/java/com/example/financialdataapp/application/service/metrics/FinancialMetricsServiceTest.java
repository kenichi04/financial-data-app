package com.example.financialdataapp.application.service.metrics;

import com.example.financialdataapp.domain.enums.Currency;
import com.example.financialdataapp.domain.enums.DisplayScale;
import com.example.financialdataapp.domain.model.DocumentId;
import com.example.financialdataapp.domain.model.EdinetCode;
import com.example.financialdataapp.domain.model.FinancialData;
import com.example.financialdataapp.domain.model.FinancialDocument;
import com.example.financialdataapp.domain.model.context.EdinetContext;
import com.example.financialdataapp.infrastructure.inmemory.InMemoryFinancialDocumentRepository;
import com.example.financialdataapp.infrastructure.inmemory.InMemoryFinancialMetricsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class FinancialMetricsServiceTest {

    private static final Long PROFIT_LOSS_ID = 1L;
    private static final Long ASSETS_ID = 2L;
    private static final Long SHAREHOLDERS_EQUITY_ID = 3L;

    private static final Map<Long, String> ACCOUNT_CODE_BY_ID = Map.of(
            PROFIT_LOSS_ID, "ProfitLoss",
            ASSETS_ID, "Assets",
            SHAREHOLDERS_EQUITY_ID, "ShareholdersEquity");

    private InMemoryFinancialDocumentRepository financialDocumentRepository;

    private InMemoryFinancialMetricsRepository financialMetricsRepository;

    private FinancialMetricsService service;

    @BeforeEach
    void setUp() {
        financialDocumentRepository = new InMemoryFinancialDocumentRepository();
        financialMetricsRepository = new InMemoryFinancialMetricsRepository();
        service = new FinancialMetricsService(
                financialDocumentRepository,
                new FinancialMetricsCalculationService(),
                financialMetricsRepository);
    }

    @Test
    void calculateAndSaveMetrics_ShouldCalculateAndSaveMetrics() {
        var documentId = new DocumentId("TEST0001");
        financialDocumentRepository.create(createDocument(documentId, "100", "1000"));

        service.calculateAndSaveMetrics(documentId, ACCOUNT_CODE_BY_ID);

        var metrics = financialMetricsRepository.findByDocumentId(documentId);
        assertNotNull(metrics);
        // ROA = 100 / 1000 × 100
        assertEquals(new BigDecimal("10.0000"), metrics.getRoa());
        // ROE = 100 / 400 × 100
        assertEquals(new BigDecimal("25.0000"), metrics.getRoe());
    }

    @Test
    void calculateAndSaveMetrics_ShouldThrow_WhenDocumentNotFound() {
        var missingId = new DocumentId("TEST9999");

        assertThrows(IllegalStateException.class,
                () -> service.calculateAndSaveMetrics(missingId, ACCOUNT_CODE_BY_ID));
        assertNull(financialMetricsRepository.findByDocumentId(missingId));
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
