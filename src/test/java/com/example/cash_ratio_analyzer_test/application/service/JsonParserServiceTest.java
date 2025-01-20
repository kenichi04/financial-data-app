package com.example.cash_ratio_analyzer_test.application.service;

import com.example.cash_ratio_analyzer_test.application.service.dto.ProcessedResponseData;
import com.example.cash_ratio_analyzer_test.domain.service.DocumentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

class JsonParserServiceTest {

    private JsonParserService jsonParserService;
    private DocumentService documentService;

    @BeforeEach
    void setUp() {
        documentService = Mockito.mock(DocumentService.class);
        jsonParserService = new JsonParserService(documentService);
    }

    @Test
    void parseDocumentList_validJsonData_returnsProcessedResponseData() {
        var jsonData = """
        {
            "metadata": {
                "resultset": {
                    "count": 1
                },
                "processDateTime": "2023-01-01 00:00",
                "status": "200"
            },
            "results": [
                {
                    "docID": "12345678",
                    "docTypeCode": "120",
                    "secCode": "1234",
                    "edinetCode": "E12345",
                    "filerName": "Test Filer",
                    "submitDateTime": "2023-01-01 00:00",
                    "docDescription": "Test Description"
                }
            ]
        }
        """;

        when(documentService.isPermittedDocumentType(anyString())).thenReturn(true);

        Optional<ProcessedResponseData> result = jsonParserService.parseDocumentList(jsonData);

        assertTrue(result.isPresent());
        assertEquals(1, result.get().getMetadataList().size());
        assertEquals("12345678", result.get().getMetadataList().get(0).getDocumentId().value());
    }
}