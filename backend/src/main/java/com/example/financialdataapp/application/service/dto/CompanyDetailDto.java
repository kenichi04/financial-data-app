package com.example.financialdataapp.application.service.dto;

import java.time.LocalDate;

/**
 * 企業詳細です（A-102）。
 *
 * @param securityCode            証券コード（4桁に正規化済み）
 * @param edinetCode              EDINETコード
 * @param name                    企業名
 * @param corporateNumber         法人番号
 * @param latestFiscalYearEndDate 直近期の決算期末日（指標未計算の場合はnull）
 */
public record CompanyDetailDto(
        String securityCode,
        String edinetCode,
        String name,
        String corporateNumber,
        LocalDate latestFiscalYearEndDate
) {
}
