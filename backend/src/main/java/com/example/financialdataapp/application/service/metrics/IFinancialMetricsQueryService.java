package com.example.financialdataapp.application.service.metrics;

import com.example.financialdataapp.domain.model.DocumentId;

import java.util.List;

public interface IFinancialMetricsQueryService {
    /** 財務指標が未計算の財務書類の書類IDを返します。 */
    List<DocumentId> findDocumentIdsWithoutMetrics();
}
