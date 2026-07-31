package com.example.financialdataapp.presentation.controller.response;

import com.example.financialdataapp.application.service.dto.CompanyDetailDto;

import java.time.LocalDate;

/**
 * 企業詳細です（A-102）。
 *
 * @param securityCode            証券コード（4桁）
 * @param edinetCode              EDINETコード
 * @param name                    企業名
 * @param corporateNumber         法人番号
 * @param latestFiscalYearEndDate 直近期の決算期末日（指標未計算の場合はnull）
 */
public record CompanyDetailResponse(
        String securityCode,
        String edinetCode,
        String name,
        String corporateNumber,
        LocalDate latestFiscalYearEndDate
) {

    public static CompanyDetailResponse from(CompanyDetailDto dto) {
        return new CompanyDetailResponse(
                dto.securityCode(),
                dto.edinetCode(),
                dto.name(),
                dto.corporateNumber(),
                dto.latestFiscalYearEndDate()
        );
    }
}
