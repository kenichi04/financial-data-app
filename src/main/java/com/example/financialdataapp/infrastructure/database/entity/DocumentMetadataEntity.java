package com.example.financialdataapp.infrastructure.database.entity;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDate;

@Entity
@Table(name = "document_metadata")
@Getter
public class DocumentMetadataEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String documentId;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private String edinetCode;

    @Column(nullable = false)
    private int documentType;

    @Column(nullable = false)
    private String formCode;

    @Column(nullable = false)
    private LocalDate submissionDate;

    @Column(nullable = false)
    private boolean processed;

    public DocumentMetadataEntity() {}

    public DocumentMetadataEntity(String documentId, String description, String edinetCode, int documentType, String formCode, LocalDate submissionDate, boolean processed) {
        this.documentId = documentId;
        this.description = description;
        this.edinetCode = edinetCode;
        this.documentType = documentType;
        this.formCode = formCode;
        this.submissionDate = submissionDate;
        this.processed = processed;
    }

    public void updateProcessedStatus() {
        // processedはfalseからtrueへの変更のみ想定している
        this.processed = true;
    }
}
