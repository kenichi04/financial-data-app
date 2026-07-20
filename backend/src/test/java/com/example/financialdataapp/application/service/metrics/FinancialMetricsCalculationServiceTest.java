package com.example.financialdataapp.application.service.metrics;

import com.example.financialdataapp.domain.enums.Currency;
import com.example.financialdataapp.domain.enums.DisplayScale;
import com.example.financialdataapp.domain.enums.context.ConsolidatedType;
import com.example.financialdataapp.domain.model.DocumentId;
import com.example.financialdataapp.domain.model.EdinetCode;
import com.example.financialdataapp.domain.model.FinancialData;
import com.example.financialdataapp.domain.model.FinancialDocument;
import com.example.financialdataapp.domain.model.context.EdinetContext;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class FinancialMetricsCalculationServiceTest {

    private static final Long PROFIT_LOSS_ID = 1L;
    private static final Long ASSETS_ID = 2L;
    private static final Long NET_ASSETS_ID = 3L;
    private static final Long SHAREHOLDERS_EQUITY_ID = 4L;
    private static final Long OPERATING_INCOME_ID = 5L;
    private static final Long NET_SALES_ID = 6L;
    private static final Long REVENUE_ID = 7L;
    private static final Long CASH_AND_DEPOSITS_ID = 8L;

    private static final Map<Long, String> ACCOUNT_CODE_BY_ID = Map.of(
            PROFIT_LOSS_ID, "ProfitLoss",
            ASSETS_ID, "Assets",
            NET_ASSETS_ID, "NetAssets",
            SHAREHOLDERS_EQUITY_ID, "ShareholdersEquity",
            OPERATING_INCOME_ID, "OperatingIncome",
            NET_SALES_ID, "NetSales",
            REVENUE_ID, "Revenue",
            CASH_AND_DEPOSITS_ID, "CashAndDeposits");

    private static final String CURRENT_INSTANT = "CurrentYearInstant";
    private static final String CURRENT_DURATION = "CurrentYearDuration";
    private static final String CURRENT_INSTANT_NON_CONSOLIDATED = "CurrentYearInstant_NonConsolidatedMember";
    private static final String CURRENT_DURATION_NON_CONSOLIDATED = "CurrentYearDuration_NonConsolidatedMember";
    private static final String PRIOR_INSTANT = "Prior1YearInstant";

    private final FinancialMetricsCalculationService service = new FinancialMetricsCalculationService();

    @Test
    void calculate_ShouldComputeAllMetrics_FromConsolidatedData() {
        var document = createDocument(List.of(
                data(PROFIT_LOSS_ID, "100", CURRENT_DURATION),
                data(ASSETS_ID, "1000", CURRENT_INSTANT),
                data(NET_ASSETS_ID, "500", CURRENT_INSTANT),
                data(SHAREHOLDERS_EQUITY_ID, "400", CURRENT_INSTANT),
                data(OPERATING_INCOME_ID, "50", CURRENT_DURATION),
                data(NET_SALES_ID, "500", CURRENT_DURATION),
                data(CASH_AND_DEPOSITS_ID, "300", CURRENT_INSTANT)));

        var metrics = service.calculate(document, ACCOUNT_CODE_BY_ID);

        assertEquals(ConsolidatedType.CONSOLIDATED, metrics.getConsolidatedType());
        // ROE = 100 / 400（株主資本を優先） × 100
        assertEquals(new BigDecimal("25.0000"), metrics.getRoe());
        // ROA = 100 / 1000 × 100
        assertEquals(new BigDecimal("10.0000"), metrics.getRoa());
        // 営業利益率 = 50 / 500 × 100
        assertEquals(new BigDecimal("10.0000"), metrics.getOperatingMargin());
        // 自己資本比率 = 500 / 1000 × 100
        assertEquals(new BigDecimal("50.0000"), metrics.getEquityRatio());
        // 現金比率 = 300 / 1000 × 100
        assertEquals(new BigDecimal("30.0000"), metrics.getCashRatio());
        assertEquals(new DocumentId("TEST0001"), metrics.getDocumentId());
        assertEquals(new EdinetCode("TEST01"), metrics.getEdinetCode());
        assertEquals(LocalDate.of(2026, 3, 31), metrics.getFiscalYearEndDate());
    }

    @Test
    void calculate_ShouldNormalizeAmountsByDisplayScale() {
        // 営業利益は円単位・売上高は千円単位: 100円 ÷ (1千円 = 1000円) × 100 = 10%
        var document = createDocument(List.of(
                data(OPERATING_INCOME_ID, "100", CURRENT_DURATION, DisplayScale.YEN),
                data(NET_SALES_ID, "1", CURRENT_DURATION, DisplayScale.THOUSAND_YEN)));

        var metrics = service.calculate(document, ACCOUNT_CODE_BY_ID);

        assertEquals(new BigDecimal("10.0000"), metrics.getOperatingMargin());
    }

    @Test
    void calculate_ShouldFallbackAccountCodes_WhenPreferredCodesAreMissing() {
        // 売上高: NetSalesが無くRevenueで報告 / 自己資本: ShareholdersEquityが無くNetAssetsで代用
        var document = createDocument(List.of(
                data(PROFIT_LOSS_ID, "100", CURRENT_DURATION),
                data(OPERATING_INCOME_ID, "50", CURRENT_DURATION),
                data(REVENUE_ID, "1000", CURRENT_DURATION),
                data(NET_ASSETS_ID, "500", CURRENT_INSTANT)));

        var metrics = service.calculate(document, ACCOUNT_CODE_BY_ID);

        // 営業利益率 = 50 / 1000（Revenue） × 100
        assertEquals(new BigDecimal("5.0000"), metrics.getOperatingMargin());
        // ROE = 100 / 500（NetAssets） × 100
        assertEquals(new BigDecimal("20.0000"), metrics.getRoe());
    }

    @Test
    void calculate_ShouldReturnNullMetrics_WhenRequiredAccountsAreMissing() {
        // 総資産が無い → ROA・自己資本比率・現金比率はnull、ROE・営業利益率は算出可能
        var document = createDocument(List.of(
                data(PROFIT_LOSS_ID, "100", CURRENT_DURATION),
                data(SHAREHOLDERS_EQUITY_ID, "400", CURRENT_INSTANT),
                data(OPERATING_INCOME_ID, "50", CURRENT_DURATION),
                data(NET_SALES_ID, "500", CURRENT_DURATION)));

        var metrics = service.calculate(document, ACCOUNT_CODE_BY_ID);

        assertNull(metrics.getRoa());
        assertNull(metrics.getEquityRatio());
        assertNull(metrics.getCashRatio());
        assertEquals(new BigDecimal("25.0000"), metrics.getRoe());
        assertEquals(new BigDecimal("10.0000"), metrics.getOperatingMargin());
    }

    @Test
    void calculate_ShouldPreferConsolidatedData_WhenBothConsolidatedAndNonConsolidatedExist() {
        var document = createDocument(List.of(
                data(PROFIT_LOSS_ID, "100", CURRENT_DURATION),
                data(SHAREHOLDERS_EQUITY_ID, "400", CURRENT_INSTANT),
                // 単体側の値は使用されないこと
                data(PROFIT_LOSS_ID, "999", CURRENT_DURATION_NON_CONSOLIDATED),
                data(SHAREHOLDERS_EQUITY_ID, "999", CURRENT_INSTANT_NON_CONSOLIDATED)));

        var metrics = service.calculate(document, ACCOUNT_CODE_BY_ID);

        assertEquals(ConsolidatedType.CONSOLIDATED, metrics.getConsolidatedType());
        assertEquals(new BigDecimal("25.0000"), metrics.getRoe());
    }

    @Test
    void calculate_ShouldUseNonConsolidatedData_WhenConsolidatedDataDoesNotExist() {
        var document = createDocument(List.of(
                data(PROFIT_LOSS_ID, "100", CURRENT_DURATION_NON_CONSOLIDATED),
                data(SHAREHOLDERS_EQUITY_ID, "400", CURRENT_INSTANT_NON_CONSOLIDATED)));

        var metrics = service.calculate(document, ACCOUNT_CODE_BY_ID);

        assertEquals(ConsolidatedType.NON_CONSOLIDATED, metrics.getConsolidatedType());
        assertEquals(new BigDecimal("25.0000"), metrics.getRoe());
    }

    @Test
    void calculate_ShouldIgnorePriorYearData() {
        // 前期の総資産のみ存在 → 当期の総資産としては使用しない
        var document = createDocument(List.of(
                data(PROFIT_LOSS_ID, "100", CURRENT_DURATION),
                data(SHAREHOLDERS_EQUITY_ID, "400", CURRENT_INSTANT),
                data(ASSETS_ID, "1000", PRIOR_INSTANT)));

        var metrics = service.calculate(document, ACCOUNT_CODE_BY_ID);

        assertNull(metrics.getRoa());
        assertEquals(new BigDecimal("25.0000"), metrics.getRoe());
    }

    @Test
    void calculate_ShouldComputeNegativePercentage_WhenNetLoss() {
        var document = createDocument(List.of(
                data(PROFIT_LOSS_ID, "-100", CURRENT_DURATION),
                data(SHAREHOLDERS_EQUITY_ID, "400", CURRENT_INSTANT)));

        var metrics = service.calculate(document, ACCOUNT_CODE_BY_ID);

        assertEquals(new BigDecimal("-25.0000"), metrics.getRoe());
    }

    private FinancialDocument createDocument(List<FinancialData> dataList) {
        return new FinancialDocument(
                new DocumentId("TEST0001"),
                new EdinetCode("TEST01"),
                "テスト様式",
                LocalDate.of(2026, 3, 31),
                Currency.JPY,
                dataList);
    }

    private FinancialData data(Long accountId, String amount, String contextRef) {
        return data(accountId, amount, contextRef, DisplayScale.YEN);
    }

    private FinancialData data(Long accountId, String amount, String contextRef, DisplayScale displayScale) {
        return new FinancialData(
                accountId,
                EdinetContext.fromContextRef(contextRef),
                new BigDecimal(amount),
                displayScale,
                Currency.JPY);
    }
}
