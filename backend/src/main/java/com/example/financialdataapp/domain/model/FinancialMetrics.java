package com.example.financialdataapp.domain.model;

import java.math.BigDecimal;

public record FinancialMetrics(
        BigDecimal roe,
        BigDecimal roa,
        BigDecimal operatingMargin,
        BigDecimal equityRatio,
        BigDecimal cashRatio) {
}
