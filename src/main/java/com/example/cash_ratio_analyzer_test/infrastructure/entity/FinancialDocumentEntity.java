package com.example.cash_ratio_analyzer_test.infrastructure.entity;

import com.example.cash_ratio_analyzer_test.domain.enums.Currency;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "financial_document")
@Getter
@Setter
public class FinancialDocumentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String documentId;

    // TODO いるか？？？
    @Column(nullable = false)
    private String edinetCode;

    @Column(nullable = false)
    private String documentType;

    @Column(nullable = false)
    private LocalDate fiscalYearEndDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Currency currency;

    @OneToMany(mappedBy = "financialDocument", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<FinancialDataEntity> data;

    public FinancialDocumentEntity() {}

    public FinancialDocumentEntity(String documentId, String edinetCode, String documentType,
                                   LocalDate fiscalYearEndDate, Currency currency, List<FinancialDataEntity> data) {
        this.documentId = documentId;
        this.edinetCode = edinetCode;
        this.documentType = documentType;
        this.fiscalYearEndDate = fiscalYearEndDate;
        this.currency = currency;
        this.data = new ArrayList<>(data);
    }
}
