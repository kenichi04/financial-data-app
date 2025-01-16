package com.example.cash_ratio_analyzer_test.domain.model;

import java.util.Objects;

public record DocumentId(String value) {
    public DocumentId {
        // 書類管理番号は半角6桁
        if (value == null || value.length() != 6 || !value.matches("[0-9a-zA-Z]+")) {
            throw new IllegalArgumentException("Document ID must be a 6-character alphanumeric string");
        }
    }

    @Override
    public String toString() {
        return value;
    }
}
