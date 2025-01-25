package com.example.cash_ratio_analyzer_test.domain.enums;

/**
 * 通貨を表す列挙型です。
 */
public enum Currency {
    JPY("JPY"),
    USD("USD");

    private final String code;

    private Currency(String code) {
        this.code = code;
    }

    public String getCode() {
        return this.code;
    }

    public static Currency fromCode(String code) {
        for (Currency currency : Currency.values()) {
            if (currency.getCode().equalsIgnoreCase(code)) {
                return currency;
            }
        }
        throw new IllegalArgumentException("Invalid currency code: " + code);
    }

}
