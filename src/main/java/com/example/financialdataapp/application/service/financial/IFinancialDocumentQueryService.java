package com.example.financialdataapp.application.service.financial;

import com.example.financialdataapp.application.service.dto.FinancialDocumentDto;
import com.example.financialdataapp.domain.model.DocumentId;

public interface IFinancialDocumentQueryService {
    FinancialDocumentDto fetchByFinancialDocumentId(DocumentId documentId);
}
