package com.example.financialdataapp.domain.enums;

/**
 * EDINET様式コードを表す列挙型です。
 */
public enum EdinetFormCode {
    // TODO 必要な様式コードを追加する
    CODE_030000("030000", "有価証券報告書", "第三号様式"),

    CODE_030001("030001", "訂正有価証券報告書", "第三号様式"),

    CODE_080000("080000", "有価証券報告書", "第八号様式");

    private final String code;

    private final String name;

    private final String number;

    private EdinetFormCode(String code, String name, String number) {
        this.code = code;
        this.name = name;
        this.number = number;
    }

    public String code() {
        return this.code;
    }

    /**
     * 指定されたコードに対応するEdinetFormCodeを返します。
     *
     * @param code EDINET様式コード
     * @return 対応するEdinetFormCode
     * @throws IllegalArgumentException 指定されたコードが不明な場合
     */
    public static EdinetFormCode fromCode(String code) {
        for (EdinetFormCode formCode : EdinetFormCode.values()) {
            if (formCode.code.equals(code)) {
                return formCode;
            }
        }
        throw new IllegalArgumentException("Unknown form code: " + code);
    }
}
