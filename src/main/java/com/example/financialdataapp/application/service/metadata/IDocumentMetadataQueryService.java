package com.example.financialdataapp.application.service.metadata;

import com.example.financialdataapp.application.service.dto.DocumentMetadataDto;

import java.util.List;

public interface IDocumentMetadataQueryService {
    List<DocumentMetadataDto> fetchUnProcessedMetadata();
}
