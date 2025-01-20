package com.example.cash_ratio_analyzer_test.domain.model;

import com.example.cash_ratio_analyzer_test.domain.enums.EdinetDocumentType;

import java.time.LocalDate;

public class FinancialDocumentMetadata {

    private DocumentId documentId;

    private String description;

    /** 提出者EDINETコード */
    private EdinetCode edinetCode;

    /** 提出者名 */
    private String filerName;

    /** 書類種別 */
    private EdinetDocumentType documentType;

    // TODO これでフィルタ掛けるか検討
    /** 様式コード */
    private String formCode;

    /** 提出日 */
    private LocalDate submissionDate;

    /** 書類取得APIで取得済か */
    private boolean processed = false;

    public FinancialDocumentMetadata(DocumentId documentId, String description, EdinetCode edinetCode, String filerName, EdinetDocumentType documentType, LocalDate submissionDate) {
        this.documentId = documentId;
        this.description = description;
        this.edinetCode = edinetCode;
        this.filerName = filerName;
        this.documentType = documentType;
        this.submissionDate = submissionDate;
        this.processed = false;
    }

    public DocumentId getDocumentId() {
        return documentId;
    }

    public String getDescription() {
        return description;
    }

    public EdinetCode getEdinetCode() {
        return edinetCode;
    }

    public boolean isProcessed() {
        return processed;
    }

    /**
     * 書類の処理ステータスを更新します。（書類取得APIで取得、保存後）
     */
    public void updateProcessedStatus() {
        this.processed = true;
    }
}
