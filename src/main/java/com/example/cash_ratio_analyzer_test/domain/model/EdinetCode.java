package com.example.cash_ratio_analyzer_test.domain.model;

public record EdinetCode(String value) {
    public EdinetCode {
        // EDINETコードは6桁の英数字
        if (value == null || value.length() != 6 || !value.matches("[0-9a-zA-Z]+")) {
            throw new IllegalArgumentException("EDINET code must be a 6-character alphanumeric string");
        }
    }

    @Override
    public String toString() {
        return value;
    }
}
