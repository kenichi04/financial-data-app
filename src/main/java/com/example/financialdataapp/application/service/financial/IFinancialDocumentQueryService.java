package com.example.financialdataapp.application.service.financial;

import com.example.financialdataapp.application.service.dto.FinancialDocumentDto;
import com.example.financialdataapp.domain.model.DocumentId;

import java.util.Optional;

public interface IFinancialDocumentQueryService {
    Optional<FinancialDocumentDto> fetchByFinancialDocumentId(DocumentId documentId);
}
