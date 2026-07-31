package com.example.financialdataapp.application.service;

import com.example.financialdataapp.application.service.metrics.FinancialMetricsService;
import com.example.financialdataapp.application.service.metrics.IFinancialMetricsQueryService;
import com.example.financialdataapp.domain.model.DocumentId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FinancialMetricsCalculationUseCaseTest {

    @Mock
    private IFinancialMetricsQueryService financialMetricsQueryService;

    @Mock
    private FinancialMetricsService financialMetricsService;

    @Mock
    private AccountService accountService;

    @InjectMocks
    private FinancialMetricsCalculationUseCase useCase;

    @Test
    void calculateAndSaveUncalculatedMetrics_ShouldCalculateAndSaveMetrics_ForAllUncalculatedDocuments() {
        var documentId1 = new DocumentId("TEST0001");
        var documentId2 = new DocumentId("TEST0002");
        when(financialMetricsQueryService.findDocumentIdsWithoutMetrics())
                .thenReturn(List.of(documentId1, documentId2));
        when(accountService.getAccounts()).thenReturn(List.of());

        var result = useCase.calculateAndSaveUncalculatedMetrics();

        assertEquals(2, result.targetCount());
        assertEquals(2, result.savedCount());
        assertTrue(result.failedDocumentIds().isEmpty());
        verify(financialMetricsService).calculateAndSaveMetrics(eq(documentId1), any());
        verify(financialMetricsService).calculateAndSaveMetrics(eq(documentId2), any());
    }

    @Test
    void calculateAndSaveUncalculatedMetrics_ShouldReturnEmptyResult_WhenNoUncalculatedDocuments() {
        when(financialMetricsQueryService.findDocumentIdsWithoutMetrics()).thenReturn(List.of());

        var result = useCase.calculateAndSaveUncalculatedMetrics();

        assertEquals(0, result.targetCount());
        assertEquals(0, result.savedCount());
        assertTrue(result.failedDocumentIds().isEmpty());
        // 未計算0件のときはマスタロードも指標計算も一切行わない（早期return）
        verifyNoInteractions(accountService);
        verifyNoInteractions(financialMetricsService);
    }

    @Test
    void calculateAndSaveUncalculatedMetrics_ShouldSkipAndRecordFailure_WhenCalculationFails() {
        var failingId = new DocumentId("TEST9999");
        var succeedingId = new DocumentId("TEST0001");
        when(financialMetricsQueryService.findDocumentIdsWithoutMetrics())
                .thenReturn(List.of(failingId, succeedingId));
        when(accountService.getAccounts()).thenReturn(List.of());
        doThrow(new IllegalStateException("FinancialDocument not found: " + failingId))
                .when(financialMetricsService).calculateAndSaveMetrics(eq(failingId), any());

        var result = useCase.calculateAndSaveUncalculatedMetrics();

        // 失敗した書類はスキップし、残りの書類の計算は継続すること
        assertEquals(2, result.targetCount());
        assertEquals(1, result.savedCount());
        assertEquals(List.of("TEST9999"), result.failedDocumentIds());
        verify(financialMetricsService).calculateAndSaveMetrics(eq(succeedingId), any());
    }
}
