package com.example.cash_ratio_analyzer.application.service.financial;

import com.example.cash_ratio_analyzer.application.service.constants.XbrlConstants;
import com.example.cash_ratio_analyzer.application.service.dto.HeaderInfo;
import com.example.cash_ratio_analyzer.application.service.metadata.DocumentMetadataService;
import com.example.cash_ratio_analyzer.domain.model.DocumentId;
import com.example.cash_ratio_analyzer.domain.model.EdinetCode;
import com.example.cash_ratio_analyzer.domain.model.FinancialData;
import com.example.cash_ratio_analyzer.domain.model.FinancialDocument;
import com.example.cash_ratio_analyzer.domain.repository.IFinancialDocumentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
public class FinancialDocumentService {

    private final DocumentMetadataService documentMetadataService;

    private final IFinancialDocumentRepository financialDocumentRepository;

    public FinancialDocumentService(DocumentMetadataService documentMetadataService, IFinancialDocumentRepository financialDocumentRepository1) {
        this.documentMetadataService = documentMetadataService;
        this.financialDocumentRepository = financialDocumentRepository1;
    }

    public FinancialDocument getFinancialDocument(String documentId) {
        return financialDocumentRepository.findByDocumentId(new DocumentId(documentId));
    }

    /**
     * 指定されたドキュメントIDに対応する財務データを登録します。
     *
     * @param documentId 登録するドキュメントのID
     * @param headerInfo ドキュメントのヘッダー情報
     * @param financialDataList 登録する財務データのリスト
     * @return 登録されたドキュメントのID
     */
    @Transactional
    public DocumentId createFinancialDocument(String documentId, HeaderInfo headerInfo, List<FinancialData> financialDataList) {
        if (financialDataList == null || financialDataList.isEmpty()) {
            throw new IllegalArgumentException("FinancialData list cannot be null or empty");
        }

        // financialDocumentは新規作成のみ、更新は不要の想定
        var documentIdModel = new DocumentId(documentId);
        var financialDocument = buildFinancialDocument(documentIdModel, headerInfo, financialDataList);

        financialDocumentRepository.create(financialDocument);
        // メタデータに該当の書類がある場合は処理済（書類取得済）に更新
        documentMetadataService.updateMetadataProcessedStatus(documentIdModel);

        return documentIdModel;
    }

    /**
     * 財務ドキュメントを作成します。
     *
     * @param documentIdModel ドキュメントIDのモデル
     * @param headerInfo ヘッダー情報
     * @param financialDataList 財務データのリスト
     * @return 作成された財務ドキュメント
     */
    private FinancialDocument buildFinancialDocument(DocumentId documentIdModel, HeaderInfo headerInfo, List<FinancialData> financialDataList) {
        var edinetCodeText = Optional.ofNullable(
                headerInfo.getDeiInfo().get(XbrlConstants.DEI_ATTRIBUTE_EDINET_CODE))
                .orElseThrow(() -> new IllegalArgumentException("Edinet Code is missing"));
        var edinetCode = new EdinetCode(edinetCodeText);
        var documentType = Optional.ofNullable(
                headerInfo.getDeiInfo().get(XbrlConstants.DEI_ATTRIBUTE_DOCUMENT_TYPE))
                .orElseThrow(() -> new IllegalArgumentException("Document Type is missing"));
        var fiscalYearEndDate = LocalDate.parse(
                Optional.ofNullable(headerInfo.getDeiInfo().get(XbrlConstants.DEI_ATTRIBUTE_CURRENT_PERIOD_END_DATE))
                        .orElseThrow(() -> new IllegalArgumentException("Current Period End Date is missing")),
                DateTimeFormatter.ofPattern("uuuu-MM-dd"));
        var currency = headerInfo.getCurrency();

        return new FinancialDocument(
                documentIdModel,
                edinetCode,
                documentType,
                fiscalYearEndDate,
                currency,
                financialDataList);
    }

    public boolean existsByDocumentId(String documentId) {
        return financialDocumentRepository.existsByDocumentId(new DocumentId(documentId));
    }
}
