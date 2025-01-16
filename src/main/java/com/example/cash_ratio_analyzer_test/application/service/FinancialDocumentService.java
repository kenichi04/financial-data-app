package com.example.cash_ratio_analyzer_test.application.service;

import com.example.cash_ratio_analyzer_test.domain.model.DocumentId;
import com.example.cash_ratio_analyzer_test.domain.model.FinancialData;
import com.example.cash_ratio_analyzer_test.domain.model.FinancialDocument;
import com.example.cash_ratio_analyzer_test.domain.repository.ICompanyRepository;
import com.example.cash_ratio_analyzer_test.domain.repository.IFinancialDocumentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FinancialDocumentService {

    private final IFinancialDocumentRepository financialDocumentRepository;

    private final ICompanyRepository companyRepository;

    public FinancialDocumentService(IFinancialDocumentRepository financialDocumentRepository1, ICompanyRepository companyRepository) {
        this.financialDocumentRepository = financialDocumentRepository1;
        this.companyRepository = companyRepository;
    }

    public FinancialDocument getFinancialDocument(String documentId) {
        return financialDocumentRepository.findByDocumentId(documentId);
    }

    // TODO transactionalアノテーションを付与する
    // 書類一覧APIレスポンスからdocument作成する場合は、dataが取得できないため、documentIdのみで作成する
    // TODO 処理見直し（たぶん不要になる）. 書類一覧APIではdocumentではなく、documentMetadataを作成する方針に変更
    public void create(String edinetCode, String documentId) {
        // TODO 直接companyRepository使うか？companyServiceでラップするか？
        var company = companyRepository.findByCompanyEdinetCode(edinetCode);
        // 仮実装, ここでcompanyIdを使用してcompanyの存在チェック
        if (company == null) {
            throw new RuntimeException("company is not found");
        }
        // companyとdocumentは一対多の関係
        // ここの処理は検討
        var financialDocument = new FinancialDocument(new DocumentId(documentId));
        company.addDocument(financialDocument);
        companyRepository.save(company);
    }

    /**
     * 指定されたドキュメントIDに対応する財務データを保存します。
     *
     * @param documentId 保存するドキュメントのID
     * @param financialDataList 保存する財務データのリスト
     */
    // TODO transactionalアノテーションを付与する
    // 書類取得APIレスポンスからの処理を想定。documentは上で作成済にするか、新規作成するかは要検討（dataなしのdocument作成してもよいのか）
    public void saveFinancialData(String documentId, List<FinancialData> financialDataList) {
        // financialDataは新規作成のみ、更新は不要の想定
        var financialDocument = new FinancialDocument(new DocumentId(documentId));
        financialDocument.createData(financialDataList);
        financialDocumentRepository.save(financialDocument);

        // メタデータを処理済に更新
        updateMetadataProcessedStatus(documentId);
    }

    /**
     * 指定されたドキュメントIDのメタデータの処理ステータスを更新します。
     *
     * @param documentId 更新するドキュメントのID
     */
    private void updateMetadataProcessedStatus(String documentId) {
        // TODO 一旦コメントアウト（nullが返るため）
        // 実運用ではmetadataテーブルから取得後にここを通るため、nullはない
//        financialDocumentMetadataService.updateMetadataProcessedStatus(documentId);
    }
}
