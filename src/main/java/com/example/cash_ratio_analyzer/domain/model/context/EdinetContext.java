package com.example.cash_ratio_analyzer.domain.model.context;

import com.example.cash_ratio_analyzer.domain.enums.context.ConsolidatedType;
import com.example.cash_ratio_analyzer.domain.enums.context.PeriodType;
import com.example.cash_ratio_analyzer.domain.enums.context.PeriodUnit;

public class EdinetContext {
    private final PeriodType periodType;
    private final PeriodUnit periodUnit;
    private final ConsolidatedType consolidatedType;
    private final String contextRef;

    private EdinetContext(PeriodType periodType, PeriodUnit periodUnit, ConsolidatedType consolidatedType, String contextRef) {
        this.periodType = periodType;
        this.periodUnit = periodUnit;
        this.consolidatedType = consolidatedType;
        this.contextRef = contextRef;
    }

    public PeriodType getPeriodType() {
        return periodType;
    }

    public PeriodUnit getPeriodUnit() {
        return periodUnit;
    }

    public ConsolidatedType getConsolidatedType() {
        return consolidatedType;
    }

    public String getContextRef() {
        return contextRef;
    }

    public static EdinetContext fromContextRef(String contextRef) {
        if (contextRef == null || contextRef.isEmpty()) {
            throw new IllegalArgumentException("contextRef must not be null or empty");
        }
        // TODO　テスト用の仮実装（すべての値をデフォルト設定）
        PeriodType periodType = PeriodType.CURRENT_YEAR;
        PeriodUnit periodUnit = PeriodUnit.INSTANT;
        ConsolidatedType consolidatedType = ConsolidatedType.CONSOLIDATED;

        return new EdinetContext(periodType, periodUnit, consolidatedType, contextRef);
    }
}
