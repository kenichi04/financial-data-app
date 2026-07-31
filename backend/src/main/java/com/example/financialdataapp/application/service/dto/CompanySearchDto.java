package com.example.financialdataapp.application.service.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 銘柄検索結果の1件です（A-101）。直近期の主要指標を同梱します。
 * 指標系は直近期の書類が無い、または計算不能な場合にnullになります。
 *
 * @param securityCode            証券コード（4桁に正規化済み）
 * @param edinetCode              EDINETコード
 * @param name                    企業名
 * @param latestFiscalYearEndDate 直近期の決算期末日
 * @param roe                     ROE（%）
 * @param roa                     ROA（%）
 * @param cashRatio               現金比率（%）
 */
public record CompanySearchDto(
        String securityCode,
        String edinetCode,
        String name,
        LocalDate latestFiscalYearEndDate,
        BigDecimal roe,
        BigDecimal roa,
        BigDecimal cashRatio
) {
}
