package com.example.cash_ratio_analyzer_test.application.service.metadata;

import com.example.cash_ratio_analyzer_test.application.service.enums.FetchMode;
import com.example.cash_ratio_analyzer_test.application.service.validation.ApiResponseValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EdinetDocumentListServiceTest {

    // テスト対象に含めたいのでMock化しない
    private ApiResponseValidator apiResponseValidator;

    @Mock
    private RestTemplate restTemplate;

    private EdinetDocumentListService edinetDocumentListService;

    @BeforeEach
    void setUp() {
        apiResponseValidator = spy(new ApiResponseValidator());
        edinetDocumentListService = new EdinetDocumentListService(
                apiResponseValidator, restTemplate, "https://example.com/api", "testKey");
    }

    @Test
    void fetchDocumentList_validContent() {
        // given
        var fromDate = LocalDate.of(2025, 1, 30);
        var formattedDate = fromDate.format(DateTimeFormatter.ofPattern("uuuu-MM-dd"));
        var fetchMode = FetchMode.METADATA_AND_LIST;

        var mockHeaders = new HttpHeaders();
        mockHeaders.setContentType(MediaType.APPLICATION_JSON);
        var mockBody = "{\"documents\": []}";
        var mockResponse = new ResponseEntity<>(mockBody, mockHeaders, HttpStatus.OK);

        when(restTemplate.exchange(
                eq("https://example.com/api"), eq(HttpMethod.GET), isNull(),
                eq(String.class), eq(formattedDate), eq(fetchMode.code()), eq("testKey")))
                .thenReturn(mockResponse);

        // when
        var response = edinetDocumentListService.fetchDocumentList(fetchMode, fromDate);

        // then
        assertEquals(mockBody, response);

        // バリデーションメソッドが呼ばれていることを確認
        verify(apiResponseValidator).validateStatusCode(mockResponse.getStatusCode());
        verify(apiResponseValidator).validateContentType(mockResponse.getHeaders().getContentType(),
                MediaType.APPLICATION_JSON);
        verify(apiResponseValidator).validateResponseBody(mockResponse.getBody());
    }

    @Test
    void fetchDocumentList_StatusCodeIsBadRequest() {
        // given
        var fromDate = LocalDate.of(2025, 1, 30);
        var formattedDate = fromDate.format(DateTimeFormatter.ofPattern("uuuu-MM-dd"));
        var fetchMode = FetchMode.METADATA_AND_LIST;

        var mockBody = "{\"documents\": []}";
        // ステータスコードがBAD_REQUESTの場合
        var mockResponse = new ResponseEntity<>(mockBody, HttpStatus.BAD_REQUEST);

        when(restTemplate.exchange(
                eq("https://example.com/api"), eq(HttpMethod.GET), isNull(),
                eq(String.class), eq(formattedDate), eq(fetchMode.code()), eq("testKey")))
                .thenReturn(mockResponse);

        assertThrows(RuntimeException.class,
                () -> edinetDocumentListService.fetchDocumentList(fetchMode, fromDate));
    }

    @Test
    void fetchDocumentList_ContentTypeIsNotJson() {
        var fromDate = LocalDate.of(2025, 1, 30);
        var formattedDate = fromDate.format(DateTimeFormatter.ofPattern("uuuu-MM-dd"));
        var fetchMode = FetchMode.METADATA_AND_LIST;

        var mockHeaders = new HttpHeaders();
        // コンテンツタイプがJSONでない場合
        mockHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        var mockBody = "{\"documents\": []}";
        var mockResponse = new ResponseEntity<>(mockBody, mockHeaders, HttpStatus.OK);

        when(restTemplate.exchange(
                eq("https://example.com/api"), eq(HttpMethod.GET), isNull(),
                eq(String.class), eq(formattedDate), eq(fetchMode.code()), eq("testKey")))
                .thenReturn(mockResponse);

        assertThrows(RuntimeException.class,
                () -> edinetDocumentListService.fetchDocumentList(fetchMode, fromDate));
    }

    @Test
    void fetchDocumentList_ResponseBodyIsNull() {
        var fromDate = LocalDate.of(2025, 1, 30);
        var formattedDate = fromDate.format(DateTimeFormatter.ofPattern("uuuu-MM-dd"));
        var fetchMode = FetchMode.METADATA_AND_LIST;

        var mockHeaders = new HttpHeaders();
        mockHeaders.setContentType(MediaType.APPLICATION_JSON);
        // レスポンスボディがnullの場合
        var mockResponse = new ResponseEntity<String>(null, mockHeaders, HttpStatus.OK);

        when(restTemplate.exchange(
                eq("https://example.com/api"), eq(HttpMethod.GET), isNull(),
                eq(String.class), eq(formattedDate), eq(fetchMode.code()), eq("testKey")))
                .thenReturn(mockResponse);

        assertThrows(RuntimeException.class,
                () -> edinetDocumentListService.fetchDocumentList(fetchMode, fromDate));
    }
}