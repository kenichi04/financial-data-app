package com.example.financialdataapp.application.service.dto;

import java.math.BigDecimal;

public record FinancialDataDto(
        Long id,
        String accountId,
        String periodType,
        String periodUnit,
        String consolidatedType,
        BigDecimal amount,
        String displayScale,
        String currency) {
}
