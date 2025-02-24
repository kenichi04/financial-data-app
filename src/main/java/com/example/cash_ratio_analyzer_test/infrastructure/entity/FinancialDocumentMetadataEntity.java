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
    private int documentType;

    @Column(nullable = false)
    private String formCode;

    @Column(nullable = false)
    private LocalDate submissionDate;

    @Column(nullable = false)
    private boolean processed;

    public FinancialDocumentMetadataEntity() {}

    public FinancialDocumentMetadataEntity(String documentId, String description, String edinetCode, String filerName, int documentType, String formCode, LocalDate submissionDate, boolean processed) {
        this.documentId = documentId;
        this.description = description;
        this.edinetCode = edinetCode;
        this.filerName = filerName;
        this.documentType = documentType;
        this.formCode = formCode;
        this.submissionDate = submissionDate;
        this.processed = processed;
    }
}
