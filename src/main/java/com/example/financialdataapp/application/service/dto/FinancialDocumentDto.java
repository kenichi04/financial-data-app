package com.example.financialdataapp.application.service.dto;

import java.time.LocalDate;

public record FinancialDocumentDto(String documentId, String edinetCode, String documentType,
                                   LocalDate fiscalYearEndDate, String currency) {

}
