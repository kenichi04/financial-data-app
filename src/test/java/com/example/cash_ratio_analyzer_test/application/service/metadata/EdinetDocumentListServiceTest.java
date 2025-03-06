package com.example.cash_ratio_analyzer_test.application.service.metadata;

import com.example.cash_ratio_analyzer_test.application.service.enums.FetchMode;
import com.example.cash_ratio_analyzer_test.application.service.validation.ApiResponseValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EdinetDocumentListServiceTest {

    @Mock
    private ApiResponseValidator apiResponseValidator;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private EdinetDocumentListService edinetDocumentListService;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(edinetDocumentListService, "edinetDocumentListUrl", "https://example.com/api");
        ReflectionTestUtils.setField(edinetDocumentListService, "subscriptionKey", "testKey");
    }

    @Test
    void fetchDocumentList_validContent() {
        var fromDate = LocalDate.of(2025, 1, 30);
        var fetchMode = FetchMode.METADATA_AND_LIST;

        var mockResponseBody = "{\"documents\": []}";

        when(restTemplate.exchange(
                anyString(), eq(HttpMethod.GET), isNull(),
                eq(String.class), any(), any(), any()))
                .thenReturn(new ResponseEntity<>(mockResponseBody, HttpStatus.OK));

        var response = edinetDocumentListService.fetchDocumentList(fetchMode, fromDate);
        assertEquals(mockResponseBody, response);
    }
}