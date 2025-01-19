package com.example.cash_ratio_analyzer_test.application.service;

import com.example.cash_ratio_analyzer_test.application.service.dto.ProcessedResponseData;
import com.example.cash_ratio_analyzer_test.domain.model.DocumentId;
import com.example.cash_ratio_analyzer_test.domain.model.FinancialDocumentMetadata;
import com.example.cash_ratio_analyzer_test.domain.repository.ICompanyRepository;
import com.example.cash_ratio_analyzer_test.domain.repository.IFinancialDocumentMetadataRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FinancialDocumentMetadataService {

    private final IFinancialDocumentMetadataRepository financialDocumentMetadataRepository;

    private final ICompanyRepository companyRepository;

    public FinancialDocumentMetadataService(IFinancialDocumentMetadataRepository financialDocumentMetadataRepository, ICompanyRepository companyRepository) {
        this.financialDocumentMetadataRepository = financialDocumentMetadataRepository;
        this.companyRepository = companyRepository;
    }

    /**
     * メタデータを作成します。
     *
     * @param processedResponseData 処理されたレスポンスデータのオプショナル
     * @return 作成されたメタデータのドキュメントIDのリスト
     */
    // TODO transactionalアノテーションを付与する
    public List<DocumentId> createMetadata(Optional<ProcessedResponseData> processedResponseData) {
        if (processedResponseData.isEmpty()) {
            return List.of();
        }

        var metadataList = processedResponseData.get().getMetadataList();
        var companies = processedResponseData.get().getCompanies();
        financialDocumentMetadataRepository.save(metadataList);
        companyRepository.save(companies);

        return metadataList.stream()
                .map(FinancialDocumentMetadata::getDocumentId)
                .toList();
    }

    /**
     * 指定されたドキュメントIDに基づいてメタデータの処理ステータスを更新します。
     *
     * @param documentId 更新するメタデータのドキュメントID
     */
    public void updateMetadataProcessedStatus(DocumentId documentId) {
        var metadata = financialDocumentMetadataRepository.findByDocumentId(documentId);
        metadata.updateProcessedStatus();
        financialDocumentMetadataRepository.save(metadata);
    }
}
