package com.example.financialdataapp.application.service;

import com.example.financialdataapp.application.service.dto.CompanyDetailDto;
import com.example.financialdataapp.application.service.dto.CompanySearchDto;
import com.example.financialdataapp.application.service.dto.PagedResult;
import com.example.financialdataapp.application.service.exception.CompanyNotFoundException;
import com.example.financialdataapp.application.service.exception.InvalidParameterException;
import com.example.financialdataapp.application.service.metadata.ICompanyQueryService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CompanyQueryUseCaseTest {

    @Mock
    private ICompanyQueryService companyQueryService;

    @InjectMocks
    private CompanyQueryUseCase useCase;

    @Test
    void searchCompanies_ShouldConvertPageToOffset_AndTrimKeyword() {
        var dto = new CompanySearchDto(
                "3083", "E03498", "スターシーズ株式会社",
                LocalDate.of(2026, 2, 28),
                new BigDecimal("3.5673"), new BigDecimal("1.2345"), new BigDecimal("28.3167"));
        when(companyQueryService.searchCompanies("スター", 40, 20))
                .thenReturn(new PagedResult<>(List.of(dto), 1));

        var result = useCase.searchCompanies("  スター  ", 2, 20);

        assertEquals(1, result.totalElements());
        assertEquals(List.of(dto), result.content());
        verify(companyQueryService).searchCompanies("スター", 40, 20);
    }

    @Test
    void searchCompanies_ShouldThrowInvalidParameter_WhenKeywordIsNull() {
        assertThrows(InvalidParameterException.class, () -> useCase.searchCompanies(null, 0, 20));
        verifyNoInteractions(companyQueryService);
    }

    @Test
    void searchCompanies_ShouldThrowInvalidParameter_WhenKeywordIsBlank() {
        assertThrows(InvalidParameterException.class, () -> useCase.searchCompanies("   ", 0, 20));
        verifyNoInteractions(companyQueryService);
    }

    @Test
    void getCompanyDetail_ShouldReturnDto_WhenCompanyExists() {
        var dto = new CompanyDetailDto(
                "3083", "E03498", "スターシーズ株式会社", "1234567890123", LocalDate.of(2026, 2, 28));
        when(companyQueryService.findBySecurityCode("3083")).thenReturn(Optional.of(dto));

        assertEquals(dto, useCase.getCompanyDetail("3083"));
    }

    @Test
    void getCompanyDetail_ShouldThrowCompanyNotFound_WhenCompanyDoesNotExist() {
        when(companyQueryService.findBySecurityCode("9999")).thenReturn(Optional.empty());

        assertThrows(CompanyNotFoundException.class, () -> useCase.getCompanyDetail("9999"));
    }
}
