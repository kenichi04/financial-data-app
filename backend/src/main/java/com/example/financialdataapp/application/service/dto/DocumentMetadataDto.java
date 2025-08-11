package com.example.financialdataapp.application.service.dto;

import java.time.LocalDate;

public record DocumentMetadataDto(
        String documentId,
        String description,
        String edinetCode,
        Integer documentType,
        String formCode,
        LocalDate submissionDate,
        boolean processed
) {
}
