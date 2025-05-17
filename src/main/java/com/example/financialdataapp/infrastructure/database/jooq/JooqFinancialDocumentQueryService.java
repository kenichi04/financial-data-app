package com.example.financialdataapp.infrastructure.database.jooq;

import com.example.financialdataapp.application.service.dto.FinancialDocumentDto;
import com.example.financialdataapp.application.service.financial.IFinancialDocumentQueryService;
import com.example.financialdataapp.domain.model.DocumentId;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static com.example.financialdataapp.tables.JooqFinancialDocument.FINANCIAL_DOCUMENT;

@Repository
public class JooqFinancialDocumentQueryService implements IFinancialDocumentQueryService {

    private final DSLContext create;

    public JooqFinancialDocumentQueryService(DSLContext create) {
        this.create = create;
    }

    @Override
    public Optional<FinancialDocumentDto> fetchByFinancialDocumentId(DocumentId documentId) {
        return create
                .select(FINANCIAL_DOCUMENT.DOCUMENT_ID,
                        FINANCIAL_DOCUMENT.EDINET_CODE,
                        FINANCIAL_DOCUMENT.DOCUMENT_TYPE,
                        FINANCIAL_DOCUMENT.FISCAL_YEAR_END_DATE,
                        FINANCIAL_DOCUMENT.CURRENCY)
                .from(FINANCIAL_DOCUMENT)
                .where(FINANCIAL_DOCUMENT.DOCUMENT_ID.eq(documentId.toString()))
                .fetchOptional()
                .map(record -> new FinancialDocumentDto(
                        record.get(FINANCIAL_DOCUMENT.DOCUMENT_ID),
                        record.get(FINANCIAL_DOCUMENT.EDINET_CODE),
                        record.get(FINANCIAL_DOCUMENT.DOCUMENT_TYPE),
                        record.get(FINANCIAL_DOCUMENT.FISCAL_YEAR_END_DATE),
                        record.get(FINANCIAL_DOCUMENT.CURRENCY)
                ));
    }
}
