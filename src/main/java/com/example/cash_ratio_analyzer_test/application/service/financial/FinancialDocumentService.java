package com.example.cash_ratio_analyzer_test.application.service.financial;

import com.example.cash_ratio_analyzer_test.application.service.constants.XbrlConstants;
import com.example.cash_ratio_analyzer_test.application.service.dto.HeaderInfo;
import com.example.cash_ratio_analyzer_test.domain.model.DocumentId;
import com.example.cash_ratio_analyzer_test.domain.model.EdinetCode;
import com.example.cash_ratio_analyzer_test.domain.model.FinancialData;
import com.example.cash_ratio_analyzer_test.domain.model.FinancialDocument;
import com.example.cash_ratio_analyzer_test.domain.repository.ICompanyRepository;
import com.example.cash_ratio_analyzer_test.domain.repository.IFinancialDocumentRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
public class FinancialDocumentService {

    private final IFinancialDocumentRepository financialDocumentRepository;

    private final ICompanyRepository companyRepository;

    public FinancialDocumentService(IFinancialDocumentRepository financialDocumentRepository1, ICompanyRepository companyRepository) {
        this.financialDocumentRepository = financialDocumentRepository1;
        this.companyRepository = companyRepository;
    }

    public FinancialDocument getFinancialDocument(String documentId) {
        return financialDocumentRepository.findByDocumentId(new DocumentId(documentId));
    }

    /**
     * 指定されたドキュメントIDに対応する財務データを保存します。
     *
     * @param documentId 保存するドキュメントのID
     * @param headerInfo ドキュメントのヘッダー情報
     * @param financialDataList 保存する財務データのリスト
     * @return 保存されたドキュメントのID
     */
    // TODO transactionalアノテーションを付与する
    // 書類取得APIレスポンスからの処理を想定。documentは上で作成済にするか、新規作成するかは要検討（dataなしのdocument作成してもよいのか）
    public DocumentId saveFinancialDocument(String documentId, HeaderInfo headerInfo, List<FinancialData> financialDataList) {
        // TODO financialDocumentの重複チェック（重複は例外をスローする）
        // financialDocumentは新規作成のみ、更新は不要の想定
        var documentIdModel = new DocumentId(documentId);
        var financialDocument = createFinancialDocument(documentIdModel, headerInfo, financialDataList);

        financialDocumentRepository.save(financialDocument);

        // メタデータを処理済に更新
        updateMetadataProcessedStatus(documentIdModel);

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
    private FinancialDocument createFinancialDocument(DocumentId documentIdModel, HeaderInfo headerInfo, List<FinancialData> financialDataList) {
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

    /**
     * 指定されたドキュメントIDのメタデータの処理ステータスを更新します。
     *
     * @param documentId 更新するドキュメントのID
     */
    private void updateMetadataProcessedStatus(DocumentId documentId) {
        // TODO 一旦コメントアウト（nullが返るため）
        // 実運用ではmetadataテーブルから取得後にここを通るため、nullはない
//        financialDocumentMetadataService.updateMetadataProcessedStatus(documentId);
    }
}
