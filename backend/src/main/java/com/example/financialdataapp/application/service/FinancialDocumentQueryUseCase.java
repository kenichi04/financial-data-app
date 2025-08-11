package com.example.financialdataapp.application.service;

import com.example.financialdataapp.application.service.dto.FinancialDocumentDto;
import com.example.financialdataapp.application.service.financial.IFinancialDocumentQueryService;
import com.example.financialdataapp.domain.model.DocumentId;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class FinancialDocumentQueryUseCase {

    private final IFinancialDocumentQueryService queryService;

    public FinancialDocumentQueryUseCase(IFinancialDocumentQueryService financialDocumentQueryService) {
        this.queryService = financialDocumentQueryService;
    }

    public Optional<FinancialDocumentDto> getFinancialDocumentDto(String documentId) {
        return queryService.fetchByFinancialDocumentId(new DocumentId(documentId));
    }
}
