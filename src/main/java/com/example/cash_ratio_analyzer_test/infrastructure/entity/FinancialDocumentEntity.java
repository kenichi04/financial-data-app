package com.example.cash_ratio_analyzer_test.infrastructure.entity;

import com.example.cash_ratio_analyzer_test.domain.enums.Currency;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
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


}
