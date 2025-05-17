package com.example.financialdataapp.application.service.dto;

import java.math.BigDecimal;

public record FinancialDataDto(
        Long accountId,
        String accountCode,
        String accountNameJp,
        String periodType,
        String periodUnit,
        String consolidatedType,
        BigDecimal amount,
        Integer displayScale,
        String currency) {
}
