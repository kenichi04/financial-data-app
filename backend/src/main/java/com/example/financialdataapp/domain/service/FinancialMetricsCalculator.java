package com.example.financialdataapp.domain.service;

import com.example.financialdataapp.domain.model.FinancialMetrics;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * 自己資本（ROE分母・自己資本比率分子）は本来 ShareholdersEquity（株主資本）だが、
 * MVPでは NetAssets（純資産）で代用している。
 */
@Service
public class FinancialMetricsCalculator {

    public FinancialMetrics calculate(
            BigDecimal netIncome, BigDecimal netAssets, BigDecimal totalAssets,
            BigDecimal operatingIncome, BigDecimal netSales, BigDecimal cash) {
        return new FinancialMetrics(
                percentageOf(netIncome, netAssets),
                percentageOf(netIncome, totalAssets),
                percentageOf(operatingIncome, netSales),
                percentageOf(netAssets, totalAssets),
                percentageOf(cash, totalAssets));
    }

    private BigDecimal percentageOf(BigDecimal numerator, BigDecimal denominator) {
        if (numerator == null || denominator == null || denominator.signum() == 0) {
            return null;
        }
        return numerator.divide(denominator, 10, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100))
                .setScale(2, RoundingMode.HALF_UP);
    }
}
