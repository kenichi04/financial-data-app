package com.example.cash_ratio_analyzer_test.infrastructure.entity;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDate;

@Entity
@Table(name = "financial_document_metadata")
@Getter
public class FinancialDocumentMetadataEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String documentId;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private String edinetCode;

    // TODO 不要（EdinetCodeを使う）
    @Column(nullable = false)
    private String filerName;

    @Column(nullable = false)
    private String documentType;

    @Column(nullable = false)
    private String formCode;

    @Column(nullable = false)
    private LocalDate submissionDate;

    @Column(nullable = false)
    private boolean processed;
}
