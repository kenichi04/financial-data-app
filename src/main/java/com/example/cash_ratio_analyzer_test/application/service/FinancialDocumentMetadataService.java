package com.example.cash_ratio_analyzer_test.application.service;

import com.example.cash_ratio_analyzer_test.application.service.dto.ProcessedResponseData;
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
    public void createMetadata(ProcessedResponseData processedResponseData) {
        // TODO companyも登録
        var metadataList = processedResponseData.getMetadataList();
        var companies = processedResponseData.getCompanies();
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
