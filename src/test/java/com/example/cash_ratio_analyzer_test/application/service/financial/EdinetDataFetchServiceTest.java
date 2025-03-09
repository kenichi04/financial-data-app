package com.example.cash_ratio_analyzer_test.application.service.financial;

import com.example.cash_ratio_analyzer_test.application.service.enums.FetchDocumentType;
import com.example.cash_ratio_analyzer_test.application.service.validation.ApiResponseValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EdinetDataFetchServiceTest {

    // テスト対象に含めたいのでMock化しない
    private ApiResponseValidator apiResponseValidator;

    @Mock
    private RestTemplate restTemplate;

    private EdinetDataFetchService edinetDataFetchService;

    @BeforeEach
    void setUp() {
        apiResponseValidator = spy(new ApiResponseValidator());
        edinetDataFetchService = new EdinetDataFetchService(
                apiResponseValidator, restTemplate, "https://example.com/api", "testKey");
    }

    @Test
    void fetchFinancialData() {
        // given
        var documentId = "TEST0001";
        var fetchDocumentType = FetchDocumentType.XBRL;

        var headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        var body = "test-body".getBytes();
        var mockResponse = new ResponseEntity<>(body, headers, HttpStatus.OK);

        when(restTemplate.exchange(
                eq("https://example.com/api"), eq(HttpMethod.GET), isNull(),
                eq(byte[].class), eq(documentId), eq(fetchDocumentType.code()), eq("testKey")))
                .thenReturn(mockResponse);

        // when
        var response = edinetDataFetchService.fetchFinancialData(fetchDocumentType, documentId);

        // then
        assertEquals(body, response);

        // バリデーションメソッドが呼ばれていることを確認
        verify(apiResponseValidator).validateStatusCode(mockResponse.getStatusCode());
        verify(apiResponseValidator).validateContentType(mockResponse.getHeaders().getContentType(),
                MediaType.APPLICATION_OCTET_STREAM, MediaType.APPLICATION_PDF);
        verify(apiResponseValidator).validateResponseBody(mockResponse.getBody());
    }
}