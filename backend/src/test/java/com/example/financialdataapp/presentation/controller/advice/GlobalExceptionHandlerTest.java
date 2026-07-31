package com.example.financialdataapp.presentation.controller.advice;

import com.example.financialdataapp.application.service.exception.CompanyNotFoundException;
import com.example.financialdataapp.application.service.exception.InvalidParameterException;
import com.example.financialdataapp.presentation.controller.response.error.ErrorCode;
import com.example.financialdataapp.presentation.controller.response.error.ErrorResponse;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.server.ResponseStatusException;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertFalse;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void handleInvalidParameter_ShouldReturn400WithInvalidParameterCode() {
        var response = handler.handleInvalidParameter(new InvalidParameterException("q は1文字以上で指定してください"));

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(ErrorCode.INVALID_PARAMETER.name(), body(response).code());
        assertEquals("q は1文字以上で指定してください", body(response).message());
    }

    @Test
    void handleConstraintViolation_ShouldReturn400_WhenViolationSetIsEmpty() {
        var response = handler.handleConstraintViolation(new ConstraintViolationException(Set.of()));

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(ErrorCode.INVALID_PARAMETER.name(), body(response).code());
        assertEquals(ErrorCode.INVALID_PARAMETER.getDefaultMessage(), body(response).message());
    }

    @Test
    void handleTypeMismatch_ShouldReturn400_WhenDateParameterIsMalformed() {
        var exception = new MethodArgumentTypeMismatchException("abc", java.time.LocalDate.class, "fromDate", null, null);

        var response = handler.handleTypeMismatch(exception);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(ErrorCode.INVALID_PARAMETER.name(), body(response).code());
    }

    @Test
    void handleMissingParameter_ShouldReturn400WithParameterName() {
        var response = handler.handleMissingParameter(new MissingServletRequestParameterException("q", "String"));

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(ErrorCode.INVALID_PARAMETER.name(), body(response).code());
        assertEquals("q は必須です", body(response).message());
    }

    @Test
    void handleCompanyNotFound_ShouldReturn404_AndNotLeakInternalMessage() {
        var response = handler.handleCompanyNotFound(new CompanyNotFoundException("9999"));

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(ErrorCode.COMPANY_NOT_FOUND.name(), body(response).code());
        assertEquals(ErrorCode.COMPANY_NOT_FOUND.getDefaultMessage(), body(response).message());
        assertFalse(body(response).message().contains("9999"));
    }

    @Test
    void handleResponseStatus_ShouldReturn404WithNotFoundCode_WhenStatusIsNotFound() {
        var exception = new ResponseStatusException(HttpStatus.NOT_FOUND, "Document not found: S100TGZR");

        var response = handler.handleResponseStatus(exception);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(ErrorCode.NOT_FOUND.name(), body(response).code());
    }

    @Test
    void handleResponseStatus_ShouldReturn500WithFixedMessage_WhenStatusIsServerError() {
        var exception = new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "downstream down");

        var response = handler.handleResponseStatus(exception);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(ErrorCode.INTERNAL_SERVER_ERROR.name(), body(response).code());
        assertEquals(ErrorCode.INTERNAL_SERVER_ERROR.getDefaultMessage(), body(response).message());
    }

    @Test
    void handleUnexpected_ShouldReturn500_AndNotLeakExceptionMessage() {
        var response = handler.handleUnexpected(new IllegalStateException("jdbc:postgresql://secret-host/db failed"));

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(ErrorCode.INTERNAL_SERVER_ERROR.name(), body(response).code());
        assertEquals(ErrorCode.INTERNAL_SERVER_ERROR.getDefaultMessage(), body(response).message());
        assertFalse(body(response).message().contains("secret-host"));
    }

    private ErrorResponse.Body body(ResponseEntity<ErrorResponse> response) {
        assertNotNull(response.getBody());
        return response.getBody().error();
    }
}
