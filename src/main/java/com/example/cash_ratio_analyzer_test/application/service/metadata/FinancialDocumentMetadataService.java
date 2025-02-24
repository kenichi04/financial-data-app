package com.example.cash_ratio_analyzer_test.application.service.metadata;

import com.example.cash_ratio_analyzer_test.application.service.dto.ProcessedResponseData;
import com.example.cash_ratio_analyzer_test.domain.model.Company;
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

    public List<FinancialDocumentMetadata> getUnprocessedMetadata() {
        // TODO 件数制限を設ける（ここで取得した書類管理番号を元に書類取得APIを呼び出す）
        return financialDocumentMetadataRepository.findByProcessedFalse();
    }

    // TODO Companyの処理が多くなったらCompanyServiceを作成する
    public List<Company> getCompanies() {
        return companyRepository.findAll();
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

        // 登録済のデータはスキップして新規のみ登録
        var newMetadataList = filterNewMetadata(metadataList);
        var newCompanies = filterNewCompanies(companies);

        financialDocumentMetadataRepository.save(newMetadataList);
        // TODO companyの処理が多くなったらCompanyServiceを作成する
        companyRepository.save(newCompanies);

        return newMetadataList.stream()
                .map(FinancialDocumentMetadata::getDocumentId)
                .toList();
    }

    /**
     * 未登録のメタデータをフィルタリングします。
     *
     * @param metadataList フィルタリングするメタデータのリスト
     * @return 未登録のメタデータのリスト
     */
    private List<FinancialDocumentMetadata> filterNewMetadata(List<FinancialDocumentMetadata> metadataList) {
        var documentIds = metadataList.stream().map(FinancialDocumentMetadata::getDocumentId).toList();

        var storedDocumentIds = financialDocumentMetadataRepository
                .findByDocumentIds(documentIds).stream()
                .map(FinancialDocumentMetadata::getDocumentId)
                .toList();
        return metadataList.stream()
                .filter(metadata -> !storedDocumentIds.contains(metadata.getDocumentId()))
                .toList();
    }

    /**
     * 未登録の会社をフィルタリングします。
     *
     * @param companies フィルタリングする会社のリスト
     * @return 未登録の会社のリスト
     */
    private List<Company> filterNewCompanies(List<Company> companies) {
        var storedCompanyEdinetCodes = companyRepository.findAll().stream()
                .map(Company::getEdinetCode)
                .toList();
        return companies.stream()
                .filter(company -> !storedCompanyEdinetCodes.contains(company.getEdinetCode()))
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
