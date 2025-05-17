package com.example.financialdataapp.infrastructure.database.jooq;

import com.example.financialdataapp.application.service.dto.FinancialDataDto;
import com.example.financialdataapp.application.service.dto.FinancialDocumentDto;
import com.example.financialdataapp.application.service.financial.IFinancialDocumentQueryService;
import com.example.financialdataapp.domain.model.DocumentId;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static com.example.financialdataapp.tables.JooqFinancialData.FINANCIAL_DATA;
import static com.example.financialdataapp.tables.JooqFinancialDocument.FINANCIAL_DOCUMENT;

@Repository
public class JooqFinancialDocumentQueryService implements IFinancialDocumentQueryService {

    private final DSLContext create;

    public JooqFinancialDocumentQueryService(DSLContext create) {
        this.create = create;
    }

    /**
     * 指定されたドキュメントIDに基づいて財務ドキュメントを取得します。
     *
     * @param documentId ドキュメントIDを表すオブジェクト
     * @return 財務ドキュメントのデータを含むOptionalオブジェクト。該当するデータが存在しない場合は空のOptionalを返します。
     */
    @Override
    public Optional<FinancialDocumentDto> fetchByFinancialDocumentId(DocumentId documentId) {
        return create
                .select(FINANCIAL_DOCUMENT.ID,
                        FINANCIAL_DOCUMENT.DOCUMENT_ID,
                        FINANCIAL_DOCUMENT.EDINET_CODE,
                        FINANCIAL_DOCUMENT.DOCUMENT_TYPE,
                        FINANCIAL_DOCUMENT.FISCAL_YEAR_END_DATE,
                        FINANCIAL_DOCUMENT.CURRENCY)
                .from(FINANCIAL_DOCUMENT)
                .where(FINANCIAL_DOCUMENT.DOCUMENT_ID.eq(documentId.toString()))
                .fetchOptional()
                .map(docRecord -> {
                    var data = create
                            .select(FINANCIAL_DATA.ID,
                                    FINANCIAL_DATA.ACCOUNT_ID,
                                    FINANCIAL_DATA.PERIOD_TYPE,
                                    FINANCIAL_DATA.PERIOD_UNIT,
                                    FINANCIAL_DATA.CONSOLIDATED_TYPE,
                                    FINANCIAL_DATA.AMOUNT,
                                    FINANCIAL_DATA.DISPLAY_SCALE,
                                    FINANCIAL_DATA.CURRENCY)
                            .from(FINANCIAL_DATA)
                            .where(FINANCIAL_DATA.FINANCIAL_DOCUMENT_ID.eq(docRecord.get(FINANCIAL_DOCUMENT.ID)))
                            .fetch()
                            .map(this::toDataDto);

                    return toDocumentDto(docRecord, data);
                });
    }

    private FinancialDocumentDto toDocumentDto(Record documentRecord, List<FinancialDataDto> data) {
        return new FinancialDocumentDto(
                documentRecord.get(FINANCIAL_DOCUMENT.ID),
                documentRecord.get(FINANCIAL_DOCUMENT.DOCUMENT_ID),
                documentRecord.get(FINANCIAL_DOCUMENT.EDINET_CODE),
                documentRecord.get(FINANCIAL_DOCUMENT.DOCUMENT_TYPE),
                documentRecord.get(FINANCIAL_DOCUMENT.FISCAL_YEAR_END_DATE),
                documentRecord.get(FINANCIAL_DOCUMENT.CURRENCY),
                data
        );
    }

    private FinancialDataDto toDataDto(Record dataRecord) {
        return new FinancialDataDto(
                dataRecord.get(FINANCIAL_DATA.ID),
                dataRecord.get(FINANCIAL_DATA.ACCOUNT_ID),
                dataRecord.get(FINANCIAL_DATA.PERIOD_TYPE),
                dataRecord.get(FINANCIAL_DATA.PERIOD_UNIT),
                dataRecord.get(FINANCIAL_DATA.CONSOLIDATED_TYPE),
                dataRecord.get(FINANCIAL_DATA.AMOUNT),
                dataRecord.get(FINANCIAL_DATA.DISPLAY_SCALE),
                dataRecord.get(FINANCIAL_DATA.CURRENCY)
        );
    }
}
