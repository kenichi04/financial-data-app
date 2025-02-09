package com.example.cash_ratio_analyzer_test.application.service.enums;

/**
 * 数値の表示単位を表す列挙型。
 * 各スケールはコードと説明を持つ。
 */
public enum DisplayScale {
    PERCENT(-2, "%単位"),
    YEN(0, "円単位"),
    THOUSAND_YEN(3, "千円単位"),
    MILLION_YEN(6, "百万円単位");

    private final int code;
    private final String description;

    private DisplayScale(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public int getCode() {
        return this.code;
    }

    public String getDescription() {
        return this.description;
    }

    public static DisplayScale fromCode(int code) {
        for (DisplayScale displayScale : DisplayScale.values()) {
            if (displayScale.getCode() == code) {
                return displayScale;
            }
        }
        throw new IllegalArgumentException("Invalid display scale code: " + code);
    }
}
