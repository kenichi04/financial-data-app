package com.example.cash_ratio_analyzer_test.application.service;

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

    public FinancialDocumentService(IFinancialDocumentRepository financialDocumentRepository, IFinancialDocumentRepository financialDocumentRepository1, ICompanyRepository companyRepository) {
        this.financialDocumentRepository = financialDocumentRepository1;
        this.companyRepository = companyRepository;
    }

    public FinancialDocument getFinancialDocument(String documentId) {
        return financialDocumentRepository.findByDocumentId(documentId);
    }

    // TODO transactionalアノテーションを付与する
    // 書類一覧APIレスポンスからdocument作成する場合は、dataが取得できないため、documentIdのみで作成する
    public void create(String companyId, String documentId) {
        // TODO 直接companyRepository使うか？companyServiceでラップするか？
        var company = companyRepository.findByCompanyId(companyId);
        // 仮実装, ここでcompanyIdを使用してcompanyの存在チェック
        if (company == null) {
            throw new RuntimeException("company is not found");
        }
        // companyとdocumentは一対多の関係
        // ここの処理は検討
        var financialDocument = new FinancialDocument(documentId);
        financialDocumentRepository.save(financialDocument);
    }

    // TODO transactionalアノテーションを付与する
    // 書類取得APIレスポンスからの処理を想定。documentは上で作成済にするか、新規作成するかは要検討（dataなしのdocument作成してもよいのか）
    public void saveFinancialData(String documentId, List<FinancialData> financialDataList) {
        // TODO documentを取得するか新規作成するかは要検討
        var financialDocument = financialDocumentRepository.findByDocumentId(documentId);
        if (financialDocument == null) {
            financialDocument = new FinancialDocument(documentId);
        }
        financialDocument.getData().addAll(financialDataList);
        financialDocumentRepository.save(financialDocument);
    }
}
