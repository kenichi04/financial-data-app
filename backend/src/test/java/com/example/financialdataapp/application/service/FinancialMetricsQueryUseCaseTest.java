package com.example.financialdataapp.application.service;

import com.example.financialdataapp.application.service.dto.FinancialDataDto;
import com.example.financialdataapp.application.service.dto.FinancialDocumentDto;
import com.example.financialdataapp.application.service.financial.FinancialAccountResolver;
import com.example.financialdataapp.application.service.financial.IFinancialDocumentQueryService;
import com.example.financialdataapp.domain.enums.context.ConsolidatedType;
import com.example.financialdataapp.domain.enums.context.PeriodUnit;
import com.example.financialdataapp.domain.enums.context.YearType;
import com.example.financialdataapp.domain.model.DocumentId;
import com.example.financialdataapp.domain.service.FinancialMetricsCalculator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FinancialMetricsQueryUseCaseTest {

    private static final String DOCUMENT_ID = "TEST0001";

    @Mock
    private IFinancialDocumentQueryService queryService;

    private FinancialMetricsQueryUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new FinancialMetricsQueryUseCase(queryService, new FinancialAccountResolver(), new FinancialMetricsCalculator());
    }

    @Test
    void getFinancialMetricsDto_ReturnsAllMetrics_WhenAllAccountsPresent() {
        var doc = documentWith(List.of(
                account("Assets", PeriodUnit.INSTANT, "2000"),
                account("NetAssets", PeriodUnit.INSTANT, "1000"),
                account("CashAndDeposits", PeriodUnit.INSTANT, "300"),
                account("OperatingIncome", PeriodUnit.DURATION, "150"),
                account("ProfitLoss", PeriodUnit.DURATION, "100"),
                account("NetSales", PeriodUnit.DURATION, "500")));
        when(queryService.fetchByFinancialDocumentId(new DocumentId(DOCUMENT_ID))).thenReturn(Optional.of(doc));

        var result = useCase.getFinancialMetricsDto(DOCUMENT_ID);

        assertTrue(result.isPresent());
        var dto = result.get();
        assertEquals(DOCUMENT_ID, dto.documentId());
        assertEquals(new BigDecimal("10.00"), dto.roe());
        assertEquals(new BigDecimal("5.00"), dto.roa());
        assertEquals(new BigDecimal("30.00"), dto.operatingMargin());
        assertEquals(new BigDecimal("50.00"), dto.equityRatio());
        assertEquals(new BigDecimal("15.00"), dto.cashRatio());
    }

    @Test
    void getFinancialMetricsDto_ReturnsPartialMetrics_WhenSomeAccountsMissing() {
        var doc = documentWith(List.of(
                account("Assets", PeriodUnit.INSTANT, "2000"),
                account("NetAssets", PeriodUnit.INSTANT, "1000")));
        when(queryService.fetchByFinancialDocumentId(new DocumentId(DOCUMENT_ID))).thenReturn(Optional.of(doc));

        var result = useCase.getFinancialMetricsDto(DOCUMENT_ID);

        assertTrue(result.isPresent());
        var dto = result.get();
        assertEquals(new BigDecimal("50.00"), dto.equityRatio());
        assertNull(dto.roe());
        assertNull(dto.roa());
        assertNull(dto.operatingMargin());
        assertNull(dto.cashRatio());
    }

    @Test
    void getFinancialMetricsDto_ReturnsEmpty_WhenDocumentNotFound() {
        when(queryService.fetchByFinancialDocumentId(new DocumentId(DOCUMENT_ID))).thenReturn(Optional.empty());

        var result = useCase.getFinancialMetricsDto(DOCUMENT_ID);

        assertTrue(result.isEmpty());
    }

    private FinancialDocumentDto documentWith(List<FinancialDataDto> data) {
        return new FinancialDocumentDto(1L, DOCUMENT_ID, "TEST01", "有価証券報告書",
                LocalDate.of(2026, 3, 31), "JPY", data);
    }

    private FinancialDataDto account(String code, PeriodUnit periodUnit, String amount) {
        return new FinancialDataDto(1L, code, code, YearType.CURRENT_YEAR.name(),
                periodUnit.name(), ConsolidatedType.CONSOLIDATED.name(), new BigDecimal(amount), 0, "JPY");
    }
}
