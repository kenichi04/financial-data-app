package com.example.financialdataapp.application.service.dto;

import java.time.LocalDate;
import java.util.List;

public record FinancialDocumentDto(
        Long id,
        String documentId,
        String edinetCode,
        String documentType,
        LocalDate fiscalYearEndDate,
        String currency,
        List<FinancialDataDto> data) {
}
