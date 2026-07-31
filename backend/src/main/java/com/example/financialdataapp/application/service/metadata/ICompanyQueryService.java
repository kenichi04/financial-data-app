package com.example.financialdataapp.application.service.metadata;

import com.example.financialdataapp.application.service.dto.CompanyDetailDto;
import com.example.financialdataapp.application.service.dto.CompanyDto;
import com.example.financialdataapp.application.service.dto.CompanySearchDto;
import com.example.financialdataapp.application.service.dto.PagedResult;

import java.util.List;
import java.util.Optional;

public interface ICompanyQueryService {

    List<CompanyDto> fetchAllCompanies();

    /**
     * 企業名の部分一致、または証券コードの前方一致で企業を検索します（A-101）。
     *
     * @param keyword 検索キーワード（1文字以上）
     * @param offset  取得開始位置（0始まり）
     * @param limit   取得件数（1以上）
     * @return 直近期の主要指標を含む検索結果と全件数
     */
    PagedResult<CompanySearchDto> searchCompanies(String keyword, int offset, int limit);

    /**
     * 証券コードで企業詳細を取得します（A-102）。
     *
     * @param securityCode 証券コード（4桁。DBには5桁で保存されているため前方一致で引き当てる）
     * @return 企業詳細。該当なしの場合は空
     */
    Optional<CompanyDetailDto> findBySecurityCode(String securityCode);
}
