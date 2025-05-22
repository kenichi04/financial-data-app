package com.example.financialdataapp.domain.model.context;

import com.example.financialdataapp.domain.enums.context.ConsolidatedType;
import com.example.financialdataapp.domain.enums.context.YearType;
import com.example.financialdataapp.domain.enums.context.PeriodUnit;

import java.util.Set;

import static com.example.financialdataapp.application.service.constants.XbrlConstants.*;

/**
 * EDINETコンテキストの定義を表すクラスです。
 *
 * このクラスは、期間タイプ、期間単位、連結タイプ、およびコンテキストIDを参照するcontextRefを管理します。
 * また、contextRefからこれらの情報を抽出するためのメソッドを提供します。
 * contextRefは、EDINETのXBRLファイルにおけるコンテキストを識別するための文字列です。
 */
public class EdinetContext {
    private final YearType yearType;
    private final PeriodUnit periodUnit;
    private final ConsolidatedType consolidatedType;
    private final String contextRef;

    private EdinetContext(YearType yearType, PeriodUnit periodUnit, ConsolidatedType consolidatedType, String contextRef) {
        this.yearType = yearType;
        this.periodUnit = periodUnit;
        this.consolidatedType = consolidatedType;
        this.contextRef = contextRef;
    }

    public YearType getPeriodType() {
        return yearType;
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
     * 指定されたcontextRefがサポートされているコンテキスト定義に含まれているかを判定します。
     *
     * <p>「SUPPORTED_CONTEXT_REFS」は、EDINETのXBRLファイルで使用されるコンテキスト定義であり、
     * BS・PL科目はこの中のいずれかの定義を持つことを想定しています。</p>
     */
    private static final Set<String> SUPPORTED_CONTEXT_REFS = Set.of(
            CONTEXT_CURRENT_YEAR_INSTANT,
            CONTEXT_CURRENT_YEAR_DURATION,
            CONTEXT_CURRENT_YEAR_INSTANT_NON_CONSOLIDATED_MEMBER,
            CONTEXT_CURRENT_YEAR_DURATION_NON_CONSOLIDATED_MEMBER
    );
    public static boolean isSupportedContextRef(String contextRef) {
        return SUPPORTED_CONTEXT_REFS.contains(contextRef);
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

        YearType yearType = extractPeriodType(contextRef);
        PeriodUnit periodUnit = extractPeriodUnit(contextRef);
        ConsolidatedType consolidatedType = extractConsolidatedType(contextRef);

        return new EdinetContext(yearType, periodUnit, consolidatedType, contextRef);
    }

    private static YearType extractPeriodType(String contextRef) {
        for (YearType type : YearType.values()) {
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
