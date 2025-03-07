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
import org.junit.jupiter.api.DisplayName;
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
    @DisplayName("ProcessedResponseData が空の場合、データを保存しない")
    void createMetadata_doesNotSaveWhenEmpty() {
        List<DocumentId> result = documentMetadataService.createMetadata(Optional.empty());

        assertEquals(List.of(), result);
        verify(documentMetadataRepository, never()).save(List.of());
        verify(companyRepository, never()).save(List.of());
    }

    @Test
    @DisplayName("新規のメタデータ・企業情報のみ保存する")
    void createMetadata_savesOnlyNewData() {
        // given
        var metadata = createTestMetadata("TEST0001", "TEST01");
        var company = createTestCompany("TEST01");
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
    @DisplayName("登録済のメタデータ・企業情報は保存しない")
    void createMetadata_filterExistingData() {
        // given
        var metadata = createTestMetadata("TEST0001", "TEST01");
        var company = createTestCompany("TEST01");

        var existingMetadata = createTestMetadata("TEST0001", "TEST01");
        var existingCompany = createTestCompany("TEST01");

        var processedResponseData = Optional.of(
                new ProcessedResponseData(List.of(company), List.of(metadata)));

        when(documentMetadataRepository.findByDocumentIds(any())).thenReturn(List.of(existingMetadata));
        when(companyRepository.findAll()).thenReturn(List.of(existingCompany));

        // when
        var result = documentMetadataService.createMetadata(processedResponseData);

        // then
        assertEquals(List.of(), result);
        verify(documentMetadataRepository, never()).save(List.of(metadata));
        verify(companyRepository, never()).save(List.of(company));
    }

    private DocumentMetadata createTestMetadata(String documentId, String edinetCode) {
        return new DocumentMetadata(
                new DocumentId(documentId),
                "test_description",
                new EdinetCode(edinetCode),
                EdinetDocumentType.YUKASHOKEN_HOKOKUSHO,
                EdinetFormCode.CODE_030000,
                LocalDate.of(2025, 1, 30));
    }

    private Company createTestCompany(String edinetCode) {
        return new Company(
                new EdinetCode(edinetCode),
                "testCompany",
                "testSecurityCode",
                "testCorporateNumber");
    }
}