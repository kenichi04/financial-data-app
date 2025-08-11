package com.example.financialdataapp.infrastructure.database.jooq;

import com.example.financialdataapp.application.service.dto.DocumentMetadataDto;
import com.example.financialdataapp.application.service.metadata.IDocumentMetadataQueryService;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.example.financialdataapp.Tables.DOCUMENT_METADATA;

@Repository
public class JooqDocumentMetadataQueryService implements IDocumentMetadataQueryService {

    private final DSLContext create;

    public JooqDocumentMetadataQueryService(DSLContext create) {
        this.create = create;
    }

    @Override
    public List<DocumentMetadataDto> fetchUnProcessedMetadata() {
        return create
                .select(
                        DOCUMENT_METADATA.DOCUMENT_ID,
                        DOCUMENT_METADATA.DESCRIPTION,
                        DOCUMENT_METADATA.EDINET_CODE,
                        DOCUMENT_METADATA.DOCUMENT_TYPE,
                        DOCUMENT_METADATA.FORM_CODE,
                        DOCUMENT_METADATA.SUBMISSION_DATE,
                        DOCUMENT_METADATA.PROCESSED
                )
                .from(DOCUMENT_METADATA)
                .where(DOCUMENT_METADATA.PROCESSED.eq(false))
                .fetch()
                .map(record -> new DocumentMetadataDto(
                        record.get(DOCUMENT_METADATA.DOCUMENT_ID),
                        record.get(DOCUMENT_METADATA.DESCRIPTION),
                        record.get(DOCUMENT_METADATA.EDINET_CODE),
                        record.get(DOCUMENT_METADATA.DOCUMENT_TYPE),
                        record.get(DOCUMENT_METADATA.FORM_CODE),
                        record.get(DOCUMENT_METADATA.SUBMISSION_DATE),
                        record.get(DOCUMENT_METADATA.PROCESSED)
                ));
    }
}
