package com.example.cash_ratio_analyzer_test.infrastructure.repository.database;

import com.example.cash_ratio_analyzer_test.domain.enums.EdinetDocumentType;
import com.example.cash_ratio_analyzer_test.domain.enums.EdinetFormCode;
import com.example.cash_ratio_analyzer_test.domain.model.DocumentId;
import com.example.cash_ratio_analyzer_test.domain.model.EdinetCode;
import com.example.cash_ratio_analyzer_test.domain.model.FinancialDocumentMetadata;
import com.example.cash_ratio_analyzer_test.domain.repository.IFinancialDocumentMetadataRepository;
import com.example.cash_ratio_analyzer_test.infrastructure.entity.FinancialDocumentMetadataEntity;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Primary
public class JpaFinancialDocumentMetadataRepository implements IFinancialDocumentMetadataRepository {

    private final FinancialDocumentMetadataRepository financialDocumentMetadataRepository;

    public JpaFinancialDocumentMetadataRepository(FinancialDocumentMetadataRepository financialDocumentMetadataRepository) {
        this.financialDocumentMetadataRepository = financialDocumentMetadataRepository;
    }

    @Override
    public FinancialDocumentMetadata findByDocumentId(DocumentId documentId) {
        var entity = financialDocumentMetadataRepository.findByDocumentId(documentId.toString());
        return toModel(entity);
    }

    @Override
    public List<FinancialDocumentMetadata> findByDocumentIds(List<DocumentId> documentIdList) {
        var documentIds = documentIdList.stream().map(DocumentId::toString).toList();
        var entities = financialDocumentMetadataRepository.findByDocumentIdIn(documentIds);
        return entities.stream().map(this::toModel).toList();
    }

    @Override
    public List<FinancialDocumentMetadata> findByProcessedFalse() {
        var entities = financialDocumentMetadataRepository.findByProcessedFalse();
        return entities.stream().map(this::toModel).toList();
    }

    @Override
    public void save(FinancialDocumentMetadata financialDocumentMetadata) {
        var entity = toEntity(financialDocumentMetadata);
        financialDocumentMetadataRepository.save(entity);
    }

    @Override
    public void save(List<FinancialDocumentMetadata> metadataList) {
        var entities = metadataList.stream().map(this::toEntity).toList();
        financialDocumentMetadataRepository.saveAll(entities);
    }

    private FinancialDocumentMetadata toModel(FinancialDocumentMetadataEntity from) {
        if (from == null) return null;

        var model =  new FinancialDocumentMetadata(
                new DocumentId(from.getDocumentId()),
                from.getDescription(),
                new EdinetCode(from.getEdinetCode()),
                from.getFilerName(),
                EdinetDocumentType.fromCode(from.getDocumentType()),
                EdinetFormCode.fromCode(from.getFormCode()),
                from.getSubmissionDate());
        if (from.isProcessed()) model.updateProcessedStatus();

        return model;
    }

    private FinancialDocumentMetadataEntity toEntity(FinancialDocumentMetadata from) {
        return new FinancialDocumentMetadataEntity(
                from.getDocumentId().toString(),
                from.getDescription(),
                from.getEdinetCode().toString(),
                from.getFilerName(),
                from.getDocumentType().code(),
                from.getFormCode().code(),
                from.getSubmissionDate(),
                from.isProcessed());
    }
}
