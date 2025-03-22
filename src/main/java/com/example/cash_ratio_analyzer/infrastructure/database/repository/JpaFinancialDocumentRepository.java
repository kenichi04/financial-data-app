package com.example.cash_ratio_analyzer.infrastructure.database.repository;

import com.example.cash_ratio_analyzer.domain.enums.Currency;
import com.example.cash_ratio_analyzer.domain.enums.DisplayScale;
import com.example.cash_ratio_analyzer.domain.model.DocumentId;
import com.example.cash_ratio_analyzer.domain.model.EdinetCode;
import com.example.cash_ratio_analyzer.domain.model.FinancialData;
import com.example.cash_ratio_analyzer.domain.model.FinancialDocument;
import com.example.cash_ratio_analyzer.domain.repository.IFinancialDocumentRepository;
import com.example.cash_ratio_analyzer.infrastructure.database.entity.FinancialDataEntity;
import com.example.cash_ratio_analyzer.infrastructure.database.entity.FinancialDocumentEntity;
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
                        jpaAccountMasterRepository.toModel(entity.getAccount()),
                        entity.getPeriodContext(),
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
                        jpaAccountMasterRepository.toEntity(data.getAccount()),
                        data.getPeriodContext(),
                        data.getAmount(),
                        data.getDisplayScale().getCode(),
                        data.getCurrency().getCode()
                ))
                .toList();
    }
}
