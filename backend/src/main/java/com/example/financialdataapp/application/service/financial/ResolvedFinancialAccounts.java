package com.example.financialdataapp.application.service.financial;

import java.math.BigDecimal;

public record ResolvedFinancialAccounts(
        BigDecimal netIncome,
        BigDecimal netAssets,
        BigDecimal totalAssets,
        BigDecimal operatingIncome,
        BigDecimal netSales,
        BigDecimal cash) {
}
