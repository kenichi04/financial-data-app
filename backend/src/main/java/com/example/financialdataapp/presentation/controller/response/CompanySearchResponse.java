package com.example.financialdataapp.presentation.controller.response;

import com.example.financialdataapp.application.service.dto.CompanySearchDto;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 銘柄検索結果の1件です（A-101）。
 *
 * @param securityCode            証券コード（4桁）
 * @param edinetCode              EDINETコード
 * @param name                    企業名
 * @param latestFiscalYearEndDate 直近期の決算期末日
 * @param latestMetrics           直近期の主要指標
 */
public record CompanySearchResponse(
        String securityCode,
        String edinetCode,
        String name,
        LocalDate latestFiscalYearEndDate,
        LatestMetrics latestMetrics
) {

    /**
     * 直近期の主要指標です。計算不能・未計算の指標はnullになります。
     *
     * @param roe       ROE（%）
     * @param roa       ROA（%）
     * @param cashRatio 現金比率（%）
     */
    public record LatestMetrics(BigDecimal roe, BigDecimal roa, BigDecimal cashRatio) {
    }

    public static CompanySearchResponse from(CompanySearchDto dto) {
        return new CompanySearchResponse(
                dto.securityCode(),
                dto.edinetCode(),
                dto.name(),
                dto.latestFiscalYearEndDate(),
                new LatestMetrics(dto.roe(), dto.roa(), dto.cashRatio())
        );
    }
}
