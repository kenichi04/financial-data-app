package com.example.financialdataapp.infrastructure.database.jooq;

import com.example.financialdataapp.application.service.dto.FinancialDocumentDto;
import com.example.financialdataapp.application.service.financial.IFinancialDocumentQueryService;
import com.example.financialdataapp.domain.model.DocumentId;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

@Repository
public class JooqFinancialDocumentQueryService implements IFinancialDocumentQueryService {

    private final DSLContext dslContext;

    public JooqFinancialDocumentQueryService(DSLContext dslContext) {
        this.dslContext = dslContext;
    }

    @Override
    public FinancialDocumentDto fetchByFinancialDocumentId(DocumentId documentId) {
        return null;
    }
}
