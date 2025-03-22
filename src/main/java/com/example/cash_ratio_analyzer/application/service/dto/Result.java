package com.example.cash_ratio_analyzer.application.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class Result {
    /** ファイル日付ごとの連番 */
    private int seqNumber;
    /** 書類管理番号 */
    private String docID;
    /** 提出者EDINETコード */
    private String edinetCode;
    /** 提出者証券コード */
    private String secCode;
    /** 提出者法人番号 */
    @JsonProperty("JCN")
    private String JCN;
    /** 提出者名 */
    private String filerName;
    private String fundCode;
    private String ordinanceCode;
    private String formCode;
    /** 書類識別コード */
    private String docTypeCode;
    /** 期間(自) */
    private String periodStart;
    /** 期間(至) */
    private String periodEnd;
    /** 提出日時 */
    private String submitDateTime;
    /** 提出書類概要 */
    private String docDescription;
    private String issuerEdinetCode;
    private String subjectEdinetCode;
    private String subsidiaryEdinetCode;
    private String currentReportReason;
    private String parentDocID;
    private String opeDateTime;
    private String withdrawalStatus;
    private String docInfoEditStatus;
    private String disclosureStatus;
    private String xbrlFlag;
    private String pdfFlag;
    private String attachDocFlag;
    private String englishDocFlag;
    private String csvFlag;
    private String legalStatus;
}
