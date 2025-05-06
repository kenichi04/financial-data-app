package com.example.cash_ratio_analyzer.infrastructure.database.entity;

import com.example.cash_ratio_analyzer.domain.enums.context.ConsolidatedType;
import com.example.cash_ratio_analyzer.domain.enums.context.PeriodType;
import com.example.cash_ratio_analyzer.domain.enums.context.PeriodUnit;
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

    @Enumerated(EnumType.STRING)
    @Column(name = "period_type", nullable = false)
    private PeriodType periodType;

    @Enumerated(EnumType.STRING)
    @Column(name = "period_unit", nullable = false)
    private PeriodUnit periodUnit;

    @Enumerated(EnumType.STRING)
    @Column(name = "consolidated_type", nullable = false)
    private ConsolidatedType consolidatedType;

    @Column(nullable = false)
    private String contextRef;

    @Column(nullable = false)
    private BigDecimal amount;

    @Column(nullable = false)
    private int displayScale;

    @Column(nullable = false)
    private String currency;

    public FinancialDataEntity() {}

    public FinancialDataEntity(FinancialDocumentEntity financialDocument, AccountMasterEntity account,
                               PeriodType periodType, PeriodUnit periodUnit, ConsolidatedType consolidatedType,
                               String contextRef, BigDecimal amount, int displayScale, String currency) {
        this.financialDocument = financialDocument;
        this.account = account;
        this.periodType = periodType;
        this.periodUnit = periodUnit;
        this.consolidatedType = consolidatedType;
        this.contextRef = contextRef;
        this.amount = amount;
        this.displayScale = displayScale;
        this.currency = currency;
    }
}
