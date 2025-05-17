package com.example.financialdataapp.application.service;

import com.example.financialdataapp.application.service.dto.FinancialDocumentDto;
import com.example.financialdataapp.application.service.financial.IFinancialDocumentQueryService;
import com.example.financialdataapp.domain.model.DocumentId;
import org.springframework.stereotype.Service;

@Service
public class FinancialDocumentQueryUseCase {

    private final IFinancialDocumentQueryService queryService;

    public FinancialDocumentQueryUseCase(IFinancialDocumentQueryService financialDocumentQueryService) {
        this.queryService = financialDocumentQueryService;
    }

    public FinancialDocumentDto getFinancialDocumentDto(String documentId) {
        return queryService.fetchByFinancialDocumentId(new DocumentId(documentId));
    }
}
