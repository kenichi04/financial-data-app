package com.example.cash_ratio_analyzer_test.application.service.financial;

import com.example.cash_ratio_analyzer_test.application.service.enums.FetchDocumentType;
import com.example.cash_ratio_analyzer_test.application.service.validation.ApiResponseValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EdinetDataFetchServiceTest {

    private ApiResponseValidator apiResponseValidator;

    @Mock
    private RestTemplate restTemplate;

    private EdinetDataFetchService edinetDataFetchService;

    @BeforeEach
    void setUp() {
        apiResponseValidator = new ApiResponseValidator();
        edinetDataFetchService = new EdinetDataFetchService(
                apiResponseValidator, restTemplate, "https://example.com/api", "testKey");
    }

    @Test
    void fetchFinancialData() {
        var documentId = "TEST0001";
        var fetchDocumentType = FetchDocumentType.XBRL;

        var headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        var body = "test-body".getBytes();
        var mockResponse = new ResponseEntity<>(body, headers, HttpStatus.OK);

        when(restTemplate.exchange(
                anyString(), eq(HttpMethod.GET), isNull(),
                eq(byte[].class), any(), any(), any()))
                .thenReturn(mockResponse);

        var response = edinetDataFetchService.fetchFinancialData(fetchDocumentType, documentId);
        assertEquals(mockResponse.getBody(), response);
    }
}