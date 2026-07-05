package com.example.financialdataapp.application.service.financial;

import com.example.financialdataapp.application.service.dto.FinancialDataDto;
import com.example.financialdataapp.domain.enums.context.ConsolidatedType;
import com.example.financialdataapp.domain.enums.context.PeriodUnit;
import com.example.financialdataapp.domain.enums.context.YearType;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class FinancialAccountResolverTest {

    private final FinancialAccountResolver resolver = new FinancialAccountResolver();

    @Test
    void resolve_ExtractsAllAccounts_WithDisplayScaleNormalization() {
        var data = List.of(
                account("Assets", PeriodUnit.INSTANT, ConsolidatedType.CONSOLIDATED, "2000", 6),
                account("NetAssets", PeriodUnit.INSTANT, ConsolidatedType.CONSOLIDATED, "1000", 6),
                account("CashAndDeposits", PeriodUnit.INSTANT, ConsolidatedType.CONSOLIDATED, "300", 6),
                account("OperatingIncome", PeriodUnit.DURATION, ConsolidatedType.CONSOLIDATED, "150", 6),
                account("ProfitLoss", PeriodUnit.DURATION, ConsolidatedType.CONSOLIDATED, "100", 6),
                account("NetSales", PeriodUnit.DURATION, ConsolidatedType.CONSOLIDATED, "500", 6));

        var resolved = resolver.resolve(data);

        assertEquals(new BigDecimal("2000000000"), resolved.totalAssets());
        assertEquals(new BigDecimal("1000000000"), resolved.netAssets());
        assertEquals(new BigDecimal("300000000"), resolved.cash());
        assertEquals(new BigDecimal("150000000"), resolved.operatingIncome());
        assertEquals(new BigDecimal("100000000"), resolved.netIncome());
        assertEquals(new BigDecimal("500000000"), resolved.netSales());
    }

    @Test
    void resolve_FallsBackToRevenue_WhenNetSalesMissing() {
        var data = List.of(
                account("Revenue", PeriodUnit.DURATION, ConsolidatedType.CONSOLIDATED, "500", 0));

        var resolved = resolver.resolve(data);

        assertEquals(new BigDecimal("500"), resolved.netSales());
    }

    @Test
    void resolve_PrefersNetSales_WhenBothNetSalesAndFallbacksPresent() {
        var data = List.of(
                account("NetSales", PeriodUnit.DURATION, ConsolidatedType.CONSOLIDATED, "500", 0),
                account("OperatingRevenue1", PeriodUnit.DURATION, ConsolidatedType.CONSOLIDATED, "999", 0));

        var resolved = resolver.resolve(data);

        assertEquals(new BigDecimal("500"), resolved.netSales());
    }

    @Test
    void resolve_PrefersConsolidated_WhenBothConsolidatedAndNonConsolidatedPresent() {
        var data = List.of(
                account("Assets", PeriodUnit.INSTANT, ConsolidatedType.NON_CONSOLIDATED, "999", 0),
                account("Assets", PeriodUnit.INSTANT, ConsolidatedType.CONSOLIDATED, "500", 0));

        var resolved = resolver.resolve(data);

        assertEquals(new BigDecimal("500"), resolved.totalAssets());
    }

    @Test
    void resolve_ExcludesPriorYearAndWrongPeriodUnit() {
        var data = List.of(
                new FinancialDataDto(1L, "Assets", "資産", YearType.PRIOR_1_YEAR.name(),
                        PeriodUnit.INSTANT.name(), ConsolidatedType.CONSOLIDATED.name(),
                        new BigDecimal("999"), 0, "JPY"),
                new FinancialDataDto(2L, "Assets", "資産", YearType.CURRENT_YEAR.name(),
                        PeriodUnit.DURATION.name(), ConsolidatedType.CONSOLIDATED.name(),
                        new BigDecimal("888"), 0, "JPY"));

        var resolved = resolver.resolve(data);

        assertNull(resolved.totalAssets());
    }

    @Test
    void resolve_ReturnsNull_WhenAccountMissing() {
        var resolved = resolver.resolve(List.of());

        assertNull(resolved.totalAssets());
        assertNull(resolved.netAssets());
        assertNull(resolved.cash());
        assertNull(resolved.operatingIncome());
        assertNull(resolved.netIncome());
        assertNull(resolved.netSales());
    }

    private FinancialDataDto account(String code, PeriodUnit periodUnit, ConsolidatedType consolidatedType,
                                      String amount, int displayScale) {
        return new FinancialDataDto(1L, code, code, YearType.CURRENT_YEAR.name(),
                periodUnit.name(), consolidatedType.name(), new BigDecimal(amount), displayScale, "JPY");
    }
}
