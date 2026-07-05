package com.example.financialdataapp.domain.service;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class FinancialMetricsCalculatorTest {

    private final FinancialMetricsCalculator calculator = new FinancialMetricsCalculator();

    @Test
    void calculate_ReturnsAllMetrics_WhenAllInputsPresent() {
        var result = calculator.calculate(
                new BigDecimal("100"),  // netIncome
                new BigDecimal("1000"), // netAssets
                new BigDecimal("2000"), // totalAssets
                new BigDecimal("150"),  // operatingIncome
                new BigDecimal("500"),  // netSales
                new BigDecimal("300")); // cash

        assertEquals(new BigDecimal("10.00"), result.roe());
        assertEquals(new BigDecimal("5.00"), result.roa());
        assertEquals(new BigDecimal("30.00"), result.operatingMargin());
        assertEquals(new BigDecimal("50.00"), result.equityRatio());
        assertEquals(new BigDecimal("15.00"), result.cashRatio());
    }

    @Test
    void calculate_ReturnsNull_WhenDenominatorIsZero() {
        var result = calculator.calculate(
                new BigDecimal("100"), BigDecimal.ZERO, BigDecimal.ZERO,
                new BigDecimal("150"), BigDecimal.ZERO, new BigDecimal("300"));

        assertNull(result.roe());
        assertNull(result.roa());
        assertNull(result.operatingMargin());
        assertNull(result.equityRatio());
        assertNull(result.cashRatio());
    }

    @Test
    void calculate_ReturnsNull_WhenNumeratorOrDenominatorIsNull() {
        var result = calculator.calculate(null, new BigDecimal("1000"), null, null, null, null);

        assertNull(result.roe());
        assertNull(result.roa());
        assertNull(result.operatingMargin());
        assertNull(result.equityRatio());
        assertNull(result.cashRatio());
    }
}
