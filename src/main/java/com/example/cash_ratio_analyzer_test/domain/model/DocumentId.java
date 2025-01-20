package com.example.cash_ratio_analyzer_test.domain.model;

public record DocumentId(String value) {
    public DocumentId {
        // 書類管理番号は半角8桁
        if (value == null || value.length() != 8 || !value.matches("[0-9a-zA-Z]+")) {
            throw new IllegalArgumentException("Document ID must be a 8-character alphanumeric string");
        }
    }

    @Override
    public String toString() {
        return value;
    }
}
