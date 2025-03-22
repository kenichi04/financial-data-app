package com.example.cash_ratio_analyzer.domain.enums;

/**
 * EDINET書類種別を表す列挙型です。
 */
public enum EdinetDocumentType {
    // TODO codeはStringの方がよい？
    /** 有価証券報告書 */
    YUKASHOKEN_HOKOKUSHO(120),
    /** 訂正有価証券報告書 */
    TEISEI_YUKASHOKEN_HOKOKUSHO(130),
    /** 四半期報告書 */
    SHIHANKI_HOKOKUSHO(140),
    /** 訂正四半期報告書 */
    TEISEI_SHIHANKI_HOKOKUSHO(150),
    /** 半期報告書 */
    HANKI_HOKOKUSHO(160),
    /** 訂正半期報告書 */
    TEISEI_HANKI_HOKOKUSHO(170);

    private final int code;

    private EdinetDocumentType(int code) {
        this.code = code;
    }

    public int code() {
        return this.code;
    }

    /**
     * 指定されたコードに対応するEdinetDocumentTypeを返します。
     *
     * @param code ドキュメントタイプを表すコード
     * @return コードに対応するEdinetDocumentType
     * @throws IllegalArgumentException 不明なコードが指定された場合
     */
    public static EdinetDocumentType fromCode(int code) {
        for (EdinetDocumentType type : EdinetDocumentType.values()) {
            if (type.code == code) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown document type code: " + code);
    }
}
