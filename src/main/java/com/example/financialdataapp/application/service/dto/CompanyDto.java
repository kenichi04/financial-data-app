package com.example.financialdataapp.application.service.dto;

public record CompanyDto(
        String edinetCode,
        String name,
        String securityCode,
        String corporateNumber
        // List<String> documentIds
) {
}
