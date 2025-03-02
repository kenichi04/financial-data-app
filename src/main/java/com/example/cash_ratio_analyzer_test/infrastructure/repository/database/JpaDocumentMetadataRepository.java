package com.example.cash_ratio_analyzer_test.infrastructure.repository.database;

import com.example.cash_ratio_analyzer_test.domain.enums.EdinetDocumentType;
import com.example.cash_ratio_analyzer_test.domain.enums.EdinetFormCode;
import com.example.cash_ratio_analyzer_test.domain.model.DocumentId;
import com.example.cash_ratio_analyzer_test.domain.model.EdinetCode;
import com.example.cash_ratio_analyzer_test.domain.model.DocumentMetadata;
import com.example.cash_ratio_analyzer_test.domain.repository.IDocumentMetadataRepository;
import com.example.cash_ratio_analyzer_test.infrastructure.entity.DocumentMetadataEntity;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Primary
public class JpaDocumentMetadataRepository implements IDocumentMetadataRepository {

    private final DocumentMetadataRepository documentMetadataRepository;

    public JpaDocumentMetadataRepository(DocumentMetadataRepository documentMetadataRepository) {
        this.documentMetadataRepository = documentMetadataRepository;
    }

    @Override
    public DocumentMetadata findByDocumentId(DocumentId documentId) {
        var entity = documentMetadataRepository.findByDocumentId(documentId.toString());
        return toModel(entity);
    }

    @Override
    public List<DocumentMetadata> findByDocumentIds(List<DocumentId> documentIdList) {
        var documentIds = documentIdList.stream().map(DocumentId::toString).toList();
        var entities = documentMetadataRepository.findByDocumentIdIn(documentIds);
        return entities.stream().map(this::toModel).toList();
    }

    @Override
    public List<DocumentMetadata> findByProcessedFalse() {
        var entities = documentMetadataRepository.findByProcessedFalse();
        return entities.stream().map(this::toModel).toList();
    }

    @Override
    public void save(DocumentMetadata documentMetadata) {
        var entity = toEntity(documentMetadata);
        documentMetadataRepository.save(entity);
    }

    @Override
    public void save(List<DocumentMetadata> metadataList) {
        var entities = metadataList.stream().map(this::toEntity).toList();
        documentMetadataRepository.saveAll(entities);
    }

    @Override
    public void updateMetadataProcessedStatus(DocumentId documentId) {
        var entity = documentMetadataRepository.findByDocumentId(documentId.toString());
        // 書類一覧APIでメタデータ取得せずに書類取得API呼ぶケースも許可したいため、エラーにせずスキップ
        if (entity == null) return;

        entity.updateProcessedStatus();
        documentMetadataRepository.save(entity);
    }

    private DocumentMetadata toModel(DocumentMetadataEntity from) {
        if (from == null) return null;

        var model =  new DocumentMetadata(
                new DocumentId(from.getDocumentId()),
                from.getDescription(),
                new EdinetCode(from.getEdinetCode()),
                EdinetDocumentType.fromCode(from.getDocumentType()),
                EdinetFormCode.fromCode(from.getFormCode()),
                from.getSubmissionDate());
        if (from.isProcessed()) model.updateProcessedStatus();

        return model;
    }

    private DocumentMetadataEntity toEntity(DocumentMetadata from) {

        return new DocumentMetadataEntity(
                from.getDocumentId().toString(),
                from.getDescription(),
                from.getEdinetCode().toString(),
                from.getDocumentType().code(),
                from.getFormCode().code(),
                from.getSubmissionDate(),
                from.isProcessed());
    }
}
