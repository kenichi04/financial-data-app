package com.example.financialdataapp.infrastructure.database.jpa.entity;

import com.example.financialdataapp.domain.enums.context.ConsolidatedType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "financial_metrics")
@Getter
@Setter
public class FinancialMetricsEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "financial_document_id", nullable = false, unique = true)
    private Long financialDocumentId;

    @Column(nullable = false)
    private String edinetCode;

    @Column(nullable = false)
    private LocalDate fiscalYearEndDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ConsolidatedType consolidatedType;

    private BigDecimal roe;

    private BigDecimal roa;

    private BigDecimal operatingMargin;

    private BigDecimal equityRatio;

    private BigDecimal cashRatio;

    public FinancialMetricsEntity() {}
}
