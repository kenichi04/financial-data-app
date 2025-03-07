package com.example.cash_ratio_analyzer_test.application.service.metadata;

import com.example.cash_ratio_analyzer_test.application.service.dto.ProcessedResponseData;
import com.example.cash_ratio_analyzer_test.domain.enums.EdinetDocumentType;
import com.example.cash_ratio_analyzer_test.domain.enums.EdinetFormCode;
import com.example.cash_ratio_analyzer_test.domain.model.Company;
import com.example.cash_ratio_analyzer_test.domain.model.DocumentId;
import com.example.cash_ratio_analyzer_test.domain.model.DocumentMetadata;
import com.example.cash_ratio_analyzer_test.domain.model.EdinetCode;
import com.example.cash_ratio_analyzer_test.domain.repository.ICompanyRepository;
import com.example.cash_ratio_analyzer_test.domain.repository.IDocumentMetadataRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DocumentMetadataServiceTest {

    @InjectMocks
    private DocumentMetadataService documentMetadataService;

    @Mock
    private IDocumentMetadataRepository documentMetadataRepository;

    @Mock
    private ICompanyRepository companyRepository;

    @BeforeEach
    void setUp() {}

    @Test
    void createMetadata() {
        // given
        var metadata = new DocumentMetadata(
                new DocumentId("TEST0001"),
                "test_description",
                new EdinetCode("Test01"),
                EdinetDocumentType.YUKASHOKEN_HOKOKUSHO,
                EdinetFormCode.CODE_030000,
                LocalDate.of(2025, 1, 30));
        var company = new Company(
                new EdinetCode("Test01"),
                "testCompany",
                "testSecurityCode",
                "testCorporateNumber");
        var processedResponseData = Optional.of(
                new ProcessedResponseData(List.of(company), List.of(metadata)));

        when(documentMetadataRepository.findByDocumentIds(any())).thenReturn(List.of());
        when(companyRepository.findAll()).thenReturn(List.of());

        // when
        var result = documentMetadataService.createMetadata(processedResponseData);

        // then
        assertEquals(List.of(new DocumentId("TEST0001")), result);
        verify(documentMetadataRepository, times(1)).save(List.of(metadata));
        verify(companyRepository, times(1)).save(List.of(company));
    }

    @Test
    void updateMetadataProcessedStatus() {
    }
}