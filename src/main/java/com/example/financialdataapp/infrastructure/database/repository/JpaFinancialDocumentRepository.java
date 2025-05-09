package com.example.financialdataapp.infrastructure.database.repository;

import com.example.financialdataapp.domain.enums.Currency;
import com.example.financialdataapp.domain.enums.DisplayScale;
import com.example.financialdataapp.domain.model.DocumentId;
import com.example.financialdataapp.domain.model.EdinetCode;
import com.example.financialdataapp.domain.model.FinancialData;
import com.example.financialdataapp.domain.model.FinancialDocument;
import com.example.financialdataapp.domain.model.context.EdinetContext;
import com.example.financialdataapp.domain.repository.IFinancialDocumentRepository;
import com.example.financialdataapp.infrastructure.database.entity.FinancialDataEntity;
import com.example.financialdataapp.infrastructure.database.entity.FinancialDocumentEntity;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Primary
public class JpaFinancialDocumentRepository implements IFinancialDocumentRepository {

    private final FinancialDocumentRepository financialDocumentRepository;

    private final FinancialDataRepository financialDataRepository;

    private final JpaAccountMasterRepository jpaAccountMasterRepository;

    public JpaFinancialDocumentRepository(FinancialDocumentRepository financialDocumentRepository, FinancialDataRepository financialDataRepository, JpaAccountMasterRepository jpaAccountMasterRepository) {
        this.financialDocumentRepository = financialDocumentRepository;
        this.financialDataRepository = financialDataRepository;
        this.jpaAccountMasterRepository = jpaAccountMasterRepository;
    }

    @Override
    public FinancialDocument findByDocumentId(DocumentId documentId) {
        var entity = financialDocumentRepository.findByDocumentId(documentId.toString());
        return toModel(entity);
    }

    @Override
    public boolean existsByDocumentId(DocumentId documentId) {
        return financialDocumentRepository.existsByDocumentId(documentId.toString());
    }

    @Override
    public void create(FinancialDocument financialDocument) {
        var entity = toNewEntity(financialDocument);
        var savedEntity = financialDocumentRepository.save(entity);

        var dataEntities = toEntityData(savedEntity, financialDocument.getData());
        savedEntity.setData(dataEntities);
        financialDataRepository.saveAll(dataEntities);
    }

    private FinancialDocument toModel(FinancialDocumentEntity from) {
        if (from == null) return null;

        return new FinancialDocument(
                new DocumentId(from.getDocumentId()),
                new EdinetCode(from.getEdinetCode()),
                from.getDocumentType(),
                from.getFiscalYearEndDate(),
                from.getCurrency(),
                toModelData(from.getData())
        );
    }

    private List<FinancialData> toModelData(List<FinancialDataEntity> from) {
        return from.stream()
                .map(entity -> new FinancialData(
                        entity.getAccountId(),
                        EdinetContext.fromContextRef(entity.getContextRef()),
                        entity.getAmount(),
                        DisplayScale.fromCode(entity.getDisplayScale()),
                        Currency.fromCode(entity.getCurrency())
                ))
                .toList();
    }

    private FinancialDocumentEntity toNewEntity(FinancialDocument from) {
        var entity = new FinancialDocumentEntity();
        entity.setDocumentId(from.getDocumentId().toString());
        entity.setEdinetCode(from.getEdinetCode().toString());
        entity.setDocumentType(from.getDocumentType());
        entity.setFiscalYearEndDate(from.getFiscalYearEndDate());
        entity.setCurrency(from.getCurrency());

        return entity;
    }

    private List<FinancialDataEntity> toEntityData(FinancialDocumentEntity documentEntity, List<FinancialData> from) {
        return from.stream()
                .map(data -> new FinancialDataEntity(
                        documentEntity,
                        data.getAccountId(),
                        data.getEdinetContext().getPeriodType(),
                        data.getEdinetContext().getPeriodUnit(),
                        data.getEdinetContext().getConsolidatedType(),
                        data.getEdinetContext().getContextRef(),
                        data.getAmount(),
                        data.getDisplayScale().getCode(),
                        data.getCurrency().getCode()
                ))
                .toList();
    }
}
