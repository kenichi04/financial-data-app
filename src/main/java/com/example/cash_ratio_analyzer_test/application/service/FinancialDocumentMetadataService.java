package com.example.cash_ratio_analyzer_test.application.service;

import com.example.cash_ratio_analyzer_test.domain.model.FinancialDocumentMetadata;
import com.example.cash_ratio_analyzer_test.domain.repository.IFinancialDocumentMetadataRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FinancialDocumentMetadataService {

    private final IFinancialDocumentMetadataRepository financialDocumentMetadataRepository;

    public FinancialDocumentMetadataService(IFinancialDocumentMetadataRepository financialDocumentMetadataRepository) {
        this.financialDocumentMetadataRepository = financialDocumentMetadataRepository;
    }

    // TODO transactionalアノテーションを付与する
    public void createMetadata(List<FinancialDocumentMetadata> metadataList) {
        // TODO companyは事前登録している想定
        financialDocumentMetadataRepository.save(metadataList);
    }

    /**
     * 指定されたドキュメントIDに基づいてメタデータの処理ステータスを更新します。
     *
     * @param documentId 更新するメタデータのドキュメントID
     */
    public void updateMetadataProcessedStatus(String documentId) {
        var metadata = financialDocumentMetadataRepository.findByDocumentId(documentId);
        metadata.updateProcessedStatus();
        financialDocumentMetadataRepository.save(metadata);
    }
}
