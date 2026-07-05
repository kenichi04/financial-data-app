package com.example.financialdataapp.application.service.dto;

import java.math.BigDecimal;

public record FinancialMetricsDto(
        String documentId,
        BigDecimal roe,
        BigDecimal roa,
        BigDecimal operatingMargin,
        BigDecimal equityRatio,
        BigDecimal cashRatio) {
}
