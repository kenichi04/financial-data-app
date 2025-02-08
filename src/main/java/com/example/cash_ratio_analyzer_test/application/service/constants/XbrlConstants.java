package com.example.cash_ratio_analyzer_test.application.service.constants;

// TODO フィールドが多くなってきたので整理したい
// メタデータ、ヘッダ情報、財務データ情報用で分ける？
public class XbrlConstants {
    /** '財務諸表本表タクソノミ'の語彙スキーマの名前空間宣言プレフィックス */
    public static final String JP_PFS_COR_NAMESPACE_PREFIX = "jppfs_cor:";

    /** '国際会計基準タクソノミ'の語彙スキーマの名前空間宣言 */
    public static final String JP_IGP_COR_NAMESPACE = "jpigp_cor:";

    /** DEI 語彙スキーマの名前空間宣言 */
    public static final String JP_DEI_NAMESPACE = "jpdei_cor:";

    /** 金額又は数値を表現するインラインXBRLの要素（タグ） */
    public static final String IX_NON_FRACTION = "ix:nonFraction";

    /** 数値以外の文字列または文章を表現するインラインXBRLの要素（タグ） */
    public static final String IX_NON_NUMERIC = "ix:nonNumeric";

    /** ヘッダ要素 > DEIの設定、提出者別タクソノミの参照、コンテキスト定義およびユニット定義の指定箇所 */
    // ix はインラインXBRLの名前空間宣言
    public static final String IX_HEADER = "ix:header";

    /** ヘッダ要素の子要素であるhidden要素. DEI及び表示しない値を定義する */
    public static final String IX_HIDDEN = "ix:hidden";

    /** ユニット定義のタグ */
    public static final String XBRLI_UNIT = "xbrli:unit";

    public static final String XBRLI_MEASURE = "xbrli:measure";

    public static final String ATTRIBUTE_ID = "id";

    /** ix:nonFraction要素の属性.報告項目となる要素を指定する */
    public static final String ATTRIBUTE_NAME = "name";

    /** ix:nonFraction要素の属性.コンテキストID（会計期間など）の参照を指定 */
    public static final String ATTRIBUTE_CONTEXT_REF = "contextRef";

    /** ix:nonFraction要素の属性.ユニットID（通貨など）の参照を指定 */
    public static final String ATTRIBUTE_UNIT_REF = "unitRef";

    /** ix:nonFraction要素の属性.数値の表示単位 */
    public static final String ATTRIBUTE_SCALE = "scale";

    /** ix:nonFraction要素の属性.負の値の場合に`-`（マイナス）を指定 */
    public static final String ATTRIBUTE_SIGN = "sign";

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

    public static final String MINUS = "-";
}
