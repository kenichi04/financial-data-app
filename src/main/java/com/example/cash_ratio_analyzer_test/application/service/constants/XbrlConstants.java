package com.example.cash_ratio_analyzer_test.application.service.constants;

// TODO フィールドが多くなってきたので整理したい
public class XbrlConstants {
    /** '財務諸表本表タクソノミ'の語彙スキーマの名前空間宣言プレフィックス */
    public static final String JP_PFS_COR_NAMESPACE_PREFIX = "jppfs_cor:";

    /** '国際会計基準タクソノミ'の語彙スキーマの名前空間宣言 */
    public static final String JP_IGP_COR_NAMESPACE = "jpigp_cor:";

    /** DEI 語彙スキーマの名前空間宣言 */
    public static final String JP_DEI_NAMESPACE = "jpdei_cor:";

    /** DEIの設定、提出者別タクソノミの参照、コンテキスト定義およびユニット定義の指定箇所 */
    // ix はインラインXBRLの名前空間宣言
    public static final String IX_HEADER = "ix:header";

    /** コンテキスト定義のタグ */
    public static final String XBRLI_CONTEXT = "xbrli:context";

    /** ユニット定義のタグ */
    public static final String XBRLI_UNIT = "xbrli:unit";

    public static final String XBRLI_MEASURE = "xbrli:measure";

    /** 金額又は数値を表現するインラインXBRLの要素 */
    public static final String IX_NON_FRACTION = "ix:nonFraction";

    public static final String ATTRIBUTE_ID = "id";

    public static final String ATTRIBUTE_NAME = "name";

    public static final String ATTRIBUTE_CONTEXT_REF = "contextRef";

    public static final String ATTRIBUTE_UNIT_REF = "unitRef";

    /** `当期連結時点`を意味するコンテキストID */
    public static final String CONTEXT_CURRENT_YEAR_INSTANT = "CurrentYearInstant";

    /** `当期連結期間`を意味するコンテキストID */
    public static final String CONTEXT_CURRENT_YEAR_DURATION = "CurrentYearDuration";

    /** `当期個別時点`を意味するコンテキストID */
    public static final String CONTEXT_CURRENT_YEAR_INSTANT_NON_CONSOLIDATED_MEMBER = "CurrentYearInstant_NonConsolidatedMember";

    /** `当期個別期間`を意味するコンテキストID */
    public static final String CONTEXT_CURRENT_YEAR_DURATION_NON_CONSOLIDATED_MEMBER = "CurrentYearDuration_NonConsolidatedMember";

    /** ユニット（通貨）のmeasure要素の値のプレフィックス */
    public static final String UNIT_ISO4217 = "iso4217:";

    public static final String COMMA = ",";
}
