package com.example.cash_ratio_analyzer.infrastructure.database.entity;

import jakarta.persistence.*;
import lombok.Getter;

import java.math.BigDecimal;

@Entity
@Table(name = "financial_data")
@Getter
public class FinancialDataEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "financial_document_id", nullable = false)
    private FinancialDocumentEntity financialDocument;

    @ManyToOne
    @JoinColumn(name = "account_id", nullable = false)
    private AccountMasterEntity account;

    @Column(nullable = false)
    private String contextId;

    @Column(nullable = false)
    private BigDecimal amount;

    @Column(nullable = false)
    private int displayScale;

    @Column(nullable = false)
    private String currency;

    public FinancialDataEntity() {}

    public FinancialDataEntity(FinancialDocumentEntity financialDocument, AccountMasterEntity account, String contextId,
                               BigDecimal amount, int displayScale, String currency) {
        this.financialDocument = financialDocument;
        this.account = account;
        this.contextId = contextId;
        this.amount = amount;
        this.displayScale = displayScale;
        this.currency = currency;
    }
}
