package com.example.cash_ratio_analyzer_test.domain.model;

import java.util.Objects;

public class EdinetCode {
    private String value;

    public EdinetCode(String value) {
        // EDINETコードは6桁の英数字
        if (value == null || value.length() != 6 || !value.matches("[0-9a-zA-Z]+")) {
            throw new IllegalArgumentException("EDINET code must be a 6-character alphanumeric string");
        }
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EdinetCode that = (EdinetCode) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return value;
    }
}
