package com.example.financialdataapp.infrastructure.database.jooq;

import com.example.financialdataapp.domain.enums.context.ConsolidatedType;
import org.jooq.DSLContext;
import org.jooq.Table;
import org.jooq.impl.DSL;

import static com.example.financialdataapp.tables.FinancialMetrics.FINANCIAL_METRICS;

/**
 * 企業ごとの「直近期の財務指標1行」を表す派生テーブルを組み立てます。
 * <p>
 * 決算期末日が最も新しい行を採用し、同一年度に連結・個別が両方ある場合は連結を優先する。
 * この選択ロジックは銘柄検索API・企業詳細API・財務指標APIで共通のため、ここに集約する。
 * <p>
 * {@code consolidated_type} は {@code @Enumerated(EnumType.STRING)} で保存されるため、
 * 比較値は {@link ConsolidatedType} のenum名を使う。
 */
final class LatestFinancialMetrics {

    /** 派生テーブルのエイリアス名 */
    static final String ALIAS = "latest_metrics";

    private LatestFinancialMetrics() {
    }

    /**
     * 直近期の指標行を企業（EDINETコード）ごとに1行だけ含む派生テーブルを返します。
     *
     * @param create DSLコンテキスト
     * @return 派生テーブル。{@code FINANCIAL_METRICS} のフィールド参照でカラムを取得できる
     */
    static Table<?> asTable(DSLContext create) {
        return create
                .select(
                        FINANCIAL_METRICS.EDINET_CODE,
                        FINANCIAL_METRICS.FISCAL_YEAR_END_DATE,
                        FINANCIAL_METRICS.CONSOLIDATED_TYPE,
                        FINANCIAL_METRICS.ROE,
                        FINANCIAL_METRICS.ROA,
                        FINANCIAL_METRICS.OPERATING_MARGIN,
                        FINANCIAL_METRICS.EQUITY_RATIO,
                        FINANCIAL_METRICS.CASH_RATIO
                )
                .distinctOn(FINANCIAL_METRICS.EDINET_CODE)
                .from(FINANCIAL_METRICS)
                .orderBy(
                        FINANCIAL_METRICS.EDINET_CODE,
                        FINANCIAL_METRICS.FISCAL_YEAR_END_DATE.desc(),
                        // 連結を先に並べることで DISTINCT ON が連結行を採用する
                        DSL.field(FINANCIAL_METRICS.CONSOLIDATED_TYPE.eq(ConsolidatedType.CONSOLIDATED.name())).desc()
                )
                .asTable(ALIAS);
    }
}
