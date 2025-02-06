package com.example.cash_ratio_analyzer_test.application.service.constants;

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

    /** ユニットのタグ */
    public static final String XBRLI_UNIT = "xbrli:unit";

    /** 金額又は数値を表現するインラインXBRLの要素 */
    public static final String IX_NON_FRACTION = "ix:nonFraction";

    public static final String ATTRIBUTE_NAME = "name";

    public static final String ATTRIBUTE_CONTEXT_REF = "contextRef";

    public static final String ATTRIBUTE_UNIT_REF = "unitRef";

    public static final String COMMA = ",";
}
