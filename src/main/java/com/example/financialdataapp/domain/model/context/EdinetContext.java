package com.example.financialdataapp.domain.model.context;

import com.example.financialdataapp.domain.enums.context.ConsolidatedType;
import com.example.financialdataapp.domain.enums.context.PeriodType;
import com.example.financialdataapp.domain.enums.context.PeriodUnit;

/**
 * EDINETコンテキストの定義を表すクラスです。
 *
 * このクラスは、期間タイプ、期間単位、連結タイプ、およびコンテキストIDを参照するcontextRefを管理します。
 * また、contextRefからこれらの情報を抽出するためのメソッドを提供します。
 * contextRefは、EDINETのXBRLファイルにおけるコンテキストを識別するための文字列です。
 */
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

    /**
     * 指定されたcontextRef文字列からEdinetContextオブジェクトを生成します。
     *
     * @param contextRef EDINETコンテキストを表す文字列。nullまたは空文字列は許可されません。
     * @return contextRefから生成されたEdinetContextオブジェクト
     * @throws IllegalArgumentException contextRefがnullまたは空の場合、または無効な形式の場合
     */
    public static EdinetContext fromContextRef(String contextRef) {
        if (contextRef == null || contextRef.isEmpty()) {
            throw new IllegalArgumentException("contextRef must not be null or empty");
        }

        PeriodType periodType = extractPeriodType(contextRef);
        PeriodUnit periodUnit = extractPeriodUnit(contextRef);
        ConsolidatedType consolidatedType = extractConsolidatedType(contextRef);

        return new EdinetContext(periodType, periodUnit, consolidatedType, contextRef);
    }

    private static PeriodType extractPeriodType(String contextRef) {
        for (PeriodType type : PeriodType.values()) {
            if (contextRef.contains(type.getLabel())) {
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid contextRef for PeriodType: " + contextRef);
    }

    private static PeriodUnit extractPeriodUnit(String contextRef) {
        for (PeriodUnit unit : PeriodUnit.values()) {
            if (contextRef.contains(unit.getLabel())) {
                return unit;
            }
        }
        throw new IllegalArgumentException("Invalid contextRef for PeriodUnit: " + contextRef);
    }

    private static ConsolidatedType extractConsolidatedType(String contextRef) {
        if (contextRef.contains("_NonConsolidatedMember")) {
            return ConsolidatedType.NON_CONSOLIDATED;
        }
        return ConsolidatedType.CONSOLIDATED;
    }
}
