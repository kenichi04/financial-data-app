package com.example.financialdataapp.application.service;

import com.example.financialdataapp.application.service.dto.CompanyDetailDto;
import com.example.financialdataapp.application.service.dto.CompanySearchDto;
import com.example.financialdataapp.application.service.dto.PagedResult;
import com.example.financialdataapp.application.service.exception.CompanyNotFoundException;
import com.example.financialdataapp.application.service.exception.InvalidParameterException;
import com.example.financialdataapp.application.service.metadata.ICompanyQueryService;
import org.springframework.stereotype.Service;

@Service
public class CompanyQueryUseCase {

    private final ICompanyQueryService queryService;

    public CompanyQueryUseCase(ICompanyQueryService companyQueryService) {
        this.queryService = companyQueryService;
    }

    /**
     * 企業名または証券コードで企業を検索します（A-101）。
     *
     * @param keyword 検索キーワード。null・空文字・空白のみは不正
     * @param page    ページ番号（0始まり）
     * @param size    1ページあたりの件数
     * @return 検索結果と全件数
     * @throws InvalidParameterException キーワードが未指定の場合
     */
    public PagedResult<CompanySearchDto> searchCompanies(String keyword, int page, int size) {
        if (keyword == null || keyword.isBlank()) {
            throw new InvalidParameterException("q は1文字以上で指定してください");
        }
        return queryService.searchCompanies(keyword.trim(), page * size, size);
    }

    /**
     * 証券コードで企業詳細を取得します（A-102）。
     *
     * @param securityCode 証券コード
     * @return 企業詳細
     * @throws CompanyNotFoundException 該当する企業が存在しない場合
     */
    public CompanyDetailDto getCompanyDetail(String securityCode) {
        return queryService.findBySecurityCode(securityCode)
                .orElseThrow(() -> new CompanyNotFoundException(securityCode));
    }
}
