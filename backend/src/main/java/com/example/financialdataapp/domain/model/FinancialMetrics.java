package com.example.financialdataapp.domain.model;

import com.example.financialdataapp.domain.enums.context.ConsolidatedType;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 1つの財務書類から算出した財務指標のセットを表すドメインモデルです。
 *
 * <p>各指標は%値（例: ROE 12.34% → 12.3400）。計算に必要な勘定科目が
 * 書類に含まれない場合、その指標はnullになります。</p>
 */
public class FinancialMetrics {
    private Long id;

    /** 算出元の書類管理番号 */
    private final DocumentId documentId;

    /** 提出者EDINETコード（companyとの論理結合キー） */
    private final EdinetCode edinetCode;

    /** 会計年度末日 */
    private final LocalDate fiscalYearEndDate;

    /** 算出に使用したデータの連結区分（連結優先） */
    private final ConsolidatedType consolidatedType;

    /** ROE = 当期純利益 ÷ 自己資本 × 100 */
    private final BigDecimal roe;

    /** ROA = 当期純利益 ÷ 総資産 × 100 */
    private final BigDecimal roa;

    /** 営業利益率 = 営業利益 ÷ 売上高 × 100 */
    private final BigDecimal operatingMargin;

    /** 自己資本比率 = 純資産 ÷ 総資産 × 100 */
    private final BigDecimal equityRatio;

    /** 現金比率 = 現金及び預金 ÷ 総資産 × 100 */
    private final BigDecimal cashRatio;

    public FinancialMetrics(DocumentId documentId, EdinetCode edinetCode, LocalDate fiscalYearEndDate,
                            ConsolidatedType consolidatedType, BigDecimal roe, BigDecimal roa,
                            BigDecimal operatingMargin, BigDecimal equityRatio, BigDecimal cashRatio) {
        if (documentId == null) {
            throw new IllegalArgumentException("DocumentId cannot be null");
        }
        if (edinetCode == null) {
            throw new IllegalArgumentException("EdinetCode cannot be null");
        }
        if (consolidatedType == null) {
            throw new IllegalArgumentException("ConsolidatedType cannot be null");
        }
        this.documentId = documentId;
        this.edinetCode = edinetCode;
        this.fiscalYearEndDate = fiscalYearEndDate;
        this.consolidatedType = consolidatedType;
        this.roe = roe;
        this.roa = roa;
        this.operatingMargin = operatingMargin;
        this.equityRatio = equityRatio;
        this.cashRatio = cashRatio;
    }

    public DocumentId getDocumentId() {
        return documentId;
    }

    public EdinetCode getEdinetCode() {
        return edinetCode;
    }

    public LocalDate getFiscalYearEndDate() {
        return fiscalYearEndDate;
    }

    public ConsolidatedType getConsolidatedType() {
        return consolidatedType;
    }

    public BigDecimal getRoe() {
        return roe;
    }

    public BigDecimal getRoa() {
        return roa;
    }

    public BigDecimal getOperatingMargin() {
        return operatingMargin;
    }

    public BigDecimal getEquityRatio() {
        return equityRatio;
    }

    public BigDecimal getCashRatio() {
        return cashRatio;
    }
}
