package com.example.financialdataapp.application.service.financial;

import com.example.financialdataapp.domain.enums.Currency;
import com.example.financialdataapp.domain.enums.DisplayScale;
import com.example.financialdataapp.domain.model.FinancialData;
import com.example.financialdataapp.domain.model.context.EdinetContext;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class FinancialDataDeduplicatorTest {

    private final FinancialDataDeduplicator deduplicator = new FinancialDataDeduplicator();

    private static final String DOC_ID = "TEST0001";

    private FinancialData data(Long accountId, String contextRef, String amount, DisplayScale scale, Currency currency) {
        return new FinancialData(
                accountId,
                EdinetContext.fromContextRef(contextRef),
                new BigDecimal(amount),
                scale,
                currency);
    }

    @Test
    void deduplicate_removesExactDuplicates_keepingFirst() {
        var first = data(18L, "CurrentYearInstant", "6470933", DisplayScale.THOUSAND_YEN, Currency.JPY);
        var dup = data(18L, "CurrentYearInstant", "6470933", DisplayScale.THOUSAND_YEN, Currency.JPY);

        var result = deduplicator.deduplicate(DOC_ID, List.of(first, dup));

        assertEquals(1, result.size());
        assertEquals(first, result.get(0));
    }

    @Test
    void deduplicate_keepsDistinctAccountsAndContexts() {
        var a = data(18L, "CurrentYearInstant", "100", DisplayScale.THOUSAND_YEN, Currency.JPY);
        var b = data(18L, "Prior1YearInstant", "200", DisplayScale.THOUSAND_YEN, Currency.JPY);
        var c = data(38L, "CurrentYearInstant", "300", DisplayScale.THOUSAND_YEN, Currency.JPY);
        // 連結と個別は context_ref が異なるため別物として残る
        var d = data(38L, "CurrentYearInstant_NonConsolidatedMember", "310", DisplayScale.THOUSAND_YEN, Currency.JPY);

        var result = deduplicator.deduplicate(DOC_ID, List.of(a, b, c, d));

        assertEquals(4, result.size());
    }

    @Test
    void deduplicate_treatsDifferentScaleButSameEffectiveValueAsDuplicate() {
        // 6470933 千円 と 6470.933 百万円 は実額が等しい → 重複として1件に集約
        var thousand = data(18L, "CurrentYearInstant", "6470933", DisplayScale.THOUSAND_YEN, Currency.JPY);
        var million = data(18L, "CurrentYearInstant", "6470.933", DisplayScale.MILLION_YEN, Currency.JPY);

        var result = deduplicator.deduplicate(DOC_ID, List.of(thousand, million));

        assertEquals(1, result.size());
        assertEquals(thousand, result.get(0));
    }

    @Test
    void deduplicate_throwsWhenSameKeyHasConflictingEffectiveAmount() {
        var one = data(18L, "CurrentYearInstant", "100", DisplayScale.THOUSAND_YEN, Currency.JPY);
        var other = data(18L, "CurrentYearInstant", "999", DisplayScale.THOUSAND_YEN, Currency.JPY);

        assertThrows(IllegalStateException.class,
                () -> deduplicator.deduplicate(DOC_ID, List.of(one, other)));
    }

    @Test
    void deduplicate_throwsWhenSameKeyHasDifferentCurrency() {
        var jpy = data(18L, "CurrentYearInstant", "100", DisplayScale.THOUSAND_YEN, Currency.JPY);
        var usd = data(18L, "CurrentYearInstant", "100", DisplayScale.THOUSAND_YEN, Currency.USD);

        assertThrows(IllegalStateException.class,
                () -> deduplicator.deduplicate(DOC_ID, List.of(jpy, usd)));
    }

    @Test
    void deduplicate_returnsEmptyForEmptyInput() {
        var result = deduplicator.deduplicate(DOC_ID, List.of());

        assertEquals(0, result.size());
    }
}
