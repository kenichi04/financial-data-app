package com.example.financialdataapp.application.service;

import com.example.financialdataapp.application.service.dto.FinancialDocumentDto;
import com.example.financialdataapp.application.service.financial.IFinancialDocumentQueryService;
import com.example.financialdataapp.domain.model.DocumentId;
import org.springframework.stereotype.Service;

@Service
public class FinancialDocumentUseCase {

    private final IFinancialDocumentQueryService queryService;

    public FinancialDocumentUseCase(IFinancialDocumentQueryService financialDocumentQueryService) {
        this.queryService = financialDocumentQueryService;
    }

    public FinancialDocumentDto getFinancialDocumentDto(String documentId) {
        return queryService.fetchByFinancialDocumentId(new DocumentId(documentId));
    }
}
