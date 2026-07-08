package com.example.financialdataapp.infrastructure.database.jooq;

import com.example.financialdataapp.application.service.metrics.IFinancialMetricsQueryService;
import com.example.financialdataapp.domain.model.DocumentId;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.example.financialdataapp.tables.FinancialDocument.FINANCIAL_DOCUMENT;
import static com.example.financialdataapp.tables.FinancialMetrics.FINANCIAL_METRICS;

@Repository
public class JooqFinancialMetricsQueryService implements IFinancialMetricsQueryService {

    private final DSLContext create;

    public JooqFinancialMetricsQueryService(DSLContext create) {
        this.create = create;
    }

    /**
     * 財務指標が未計算の財務書類の書類IDを取得します。
     *
     * @return 未計算の書類IDのリスト（取込順）
     */
    @Override
    public List<DocumentId> findDocumentIdsWithoutMetrics() {
        return create
                .select(FINANCIAL_DOCUMENT.DOCUMENT_ID)
                .from(FINANCIAL_DOCUMENT)
                .leftJoin(FINANCIAL_METRICS)
                .on(FINANCIAL_METRICS.FINANCIAL_DOCUMENT_ID.eq(FINANCIAL_DOCUMENT.ID))
                .where(FINANCIAL_METRICS.ID.isNull())
                .orderBy(FINANCIAL_DOCUMENT.ID)
                .fetch(FINANCIAL_DOCUMENT.DOCUMENT_ID)
                .stream()
                .map(DocumentId::new)
                .toList();
    }
}
