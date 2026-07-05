package com.example.financialdataapp.application.service.financial;

import com.example.financialdataapp.application.service.dto.FinancialDataDto;
import com.example.financialdataapp.domain.enums.context.ConsolidatedType;
import com.example.financialdataapp.domain.enums.context.PeriodUnit;
import com.example.financialdataapp.domain.enums.context.YearType;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class FinancialAccountResolver {

    private static final String ACCOUNT_ASSETS = "Assets";
    private static final String ACCOUNT_NET_ASSETS = "NetAssets";
    private static final String ACCOUNT_CASH = "CashAndDeposits";
    private static final String ACCOUNT_OPERATING_INCOME = "OperatingIncome";
    private static final String ACCOUNT_PROFIT_LOSS = "ProfitLoss";
    private static final List<String> ACCOUNT_NET_SALES_FALLBACK =
            List.of("NetSales", "Revenue", "OperatingRevenue1", "OperatingRevenue2");

    public ResolvedFinancialAccounts resolve(List<FinancialDataDto> data) {
        BigDecimal totalAssets = findAmount(data, List.of(ACCOUNT_ASSETS), PeriodUnit.INSTANT).orElse(null);
        BigDecimal netAssets = findAmount(data, List.of(ACCOUNT_NET_ASSETS), PeriodUnit.INSTANT).orElse(null);
        BigDecimal cash = findAmount(data, List.of(ACCOUNT_CASH), PeriodUnit.INSTANT).orElse(null);
        BigDecimal operatingIncome = findAmount(data, List.of(ACCOUNT_OPERATING_INCOME), PeriodUnit.DURATION).orElse(null);
        BigDecimal netIncome = findAmount(data, List.of(ACCOUNT_PROFIT_LOSS), PeriodUnit.DURATION).orElse(null);
        BigDecimal netSales = findAmount(data, ACCOUNT_NET_SALES_FALLBACK, PeriodUnit.DURATION).orElse(null);

        return new ResolvedFinancialAccounts(netIncome, netAssets, totalAssets, operatingIncome, netSales, cash);
    }

    private Optional<BigDecimal> findAmount(List<FinancialDataDto> data, List<String> candidateCodes, PeriodUnit expectedPeriodUnit) {
        List<FinancialDataDto> filtered = data.stream()
                .filter(d -> d.yearType().equals(YearType.CURRENT_YEAR.name()))
                .filter(d -> d.periodUnit().equals(expectedPeriodUnit.name()))
                .toList();

        for (String code : candidateCodes) {
            List<FinancialDataDto> matches = filtered.stream()
                    .filter(d -> d.accountCode().equals(code))
                    .toList();
            if (matches.isEmpty()) {
                continue;
            }
            FinancialDataDto chosen = matches.stream()
                    .filter(d -> d.consolidatedType().equals(ConsolidatedType.CONSOLIDATED.name()))
                    .findFirst()
                    .orElseGet(() -> matches.get(0));
            return Optional.of(normalize(chosen));
        }
        return Optional.empty();
    }

    private BigDecimal normalize(FinancialDataDto dto) {
        return dto.amount().movePointRight(dto.displayScale());
    }
}
