package com.example.financialdataapp.presentation.controller.response;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PageResponseTest {

    @Test
    void of_ShouldReturnZeroTotalPages_WhenNoElements() {
        var response = PageResponse.of(List.of(), 0, 0, 20);

        assertEquals(0, response.totalPages());
        assertEquals(0, response.totalElements());
        assertEquals(List.of(), response.content());
    }

    @Test
    void of_ShouldRoundUpTotalPages_WhenTotalIsNotDivisibleBySize() {
        var response = PageResponse.of(List.of("a"), 150, 0, 20);

        assertEquals(8, response.totalPages());
    }

    @Test
    void of_ShouldNotAddExtraPage_WhenTotalIsDivisibleBySize() {
        var response = PageResponse.of(List.of("a"), 40, 1, 20);

        assertEquals(2, response.totalPages());
        assertEquals(1, response.page());
        assertEquals(20, response.size());
    }

    @Test
    void of_ShouldReturnOnePage_WhenTotalIsSmallerThanSize() {
        var response = PageResponse.of(List.of("a"), 3, 0, 20);

        assertEquals(1, response.totalPages());
    }

    @Test
    void of_ShouldThrow_WhenSizeIsNotPositive() {
        assertThrows(IllegalArgumentException.class, () -> PageResponse.of(List.of(), 10, 0, 0));
    }
}
