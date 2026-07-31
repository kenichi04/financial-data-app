package com.example.financialdataapp.application.service.metadata;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class SecurityCodeNormalizerTest {

    @Test
    void toMarketCode_ShouldDropTrailingZero_WhenStoredCodeIsFiveDigits() {
        assertEquals("3083", SecurityCodeNormalizer.toMarketCode("30830"));
        assertEquals("7203", SecurityCodeNormalizer.toMarketCode("72030"));
    }

    @Test
    void toMarketCode_ShouldReturnAsIs_WhenStoredCodeIsAlreadyFourDigits() {
        assertEquals("3083", SecurityCodeNormalizer.toMarketCode("3083"));
    }

    @Test
    void toMarketCode_ShouldReturnAsIs_WhenFiveDigitsDoNotEndWithZero() {
        assertEquals("30831", SecurityCodeNormalizer.toMarketCode("30831"));
    }

    @Test
    void toMarketCode_ShouldReturnNull_WhenInputIsNull() {
        assertNull(SecurityCodeNormalizer.toMarketCode(null));
    }
}
