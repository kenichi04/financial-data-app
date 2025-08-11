package com.example.financialdataapp.domain.model;

import com.example.financialdataapp.domain.enums.EdinetDocumentType;
import com.example.financialdataapp.domain.enums.EdinetFormCode;

import java.time.LocalDate;

public class DocumentMetadata {
    private Long id;

    private DocumentId documentId;

    private String description;

    /** 提出者EDINETコード */
    private EdinetCode edinetCode;

    /** 書類種別 */
    private EdinetDocumentType documentType;

    // TODO これでフィルタ掛けるか検討
    /** 様式コード */
    private EdinetFormCode formCode;

    // TODO 会計年度あった方が良さそう
    /** 提出日 */
    private LocalDate submissionDate;

    /** 書類取得APIで取得済か */
    private boolean processed = false;

    // TODO 書類取得で失敗したかのステータスも追加する？もしくはリトライ回数を追加する？
    public DocumentMetadata(DocumentId documentId, String description, EdinetCode edinetCode, EdinetDocumentType documentType, EdinetFormCode formCode, LocalDate submissionDate) {
        this.documentId = documentId;
        this.description = description;
        this.edinetCode = edinetCode;
        this.documentType = documentType;
        this.formCode = formCode;
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

    public EdinetDocumentType getDocumentType() {
        return documentType;
    }

    public EdinetFormCode getFormCode() {
        return formCode;
    }

    public LocalDate getSubmissionDate() {
        return submissionDate;
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
