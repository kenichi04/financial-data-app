package com.example.financialdataapp.presentation.controller;

import com.example.financialdataapp.application.service.CompanyQueryUseCase;
import com.example.financialdataapp.application.service.dto.CompanySearchDto;
import com.example.financialdataapp.application.service.dto.PagedResult;
import com.example.financialdataapp.presentation.controller.response.CompanyDetailResponse;
import com.example.financialdataapp.presentation.controller.response.CompanySearchResponse;
import com.example.financialdataapp.presentation.controller.response.PageResponse;
import com.example.financialdataapp.presentation.controller.response.error.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/companies")
@Validated
public class CompanyController {

    /** 1ページあたりの最大件数（docs/api_spec_draft.md「ページネーション」） */
    private static final int MAX_PAGE_SIZE = 100;

    private final CompanyQueryUseCase companyQueryUseCase;

    public CompanyController(CompanyQueryUseCase companyQueryUseCase) {
        this.companyQueryUseCase = companyQueryUseCase;
    }

    @Operation(
            summary = "銘柄検索（A-101）",
            description = "企業名の部分一致、または証券コードの前方一致で検索し、直近期の主要指標を添えて返す"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "検索成功"),
            @ApiResponse(
                    responseCode = "400",
                    description = "q が未指定、または page / size が範囲外",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    @GetMapping
    public PageResponse<CompanySearchResponse> search(
            @RequestParam(required = false) String q,
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "20") @Min(1) @Max(MAX_PAGE_SIZE) int size
    ) {
        PagedResult<CompanySearchDto> result = companyQueryUseCase.searchCompanies(q, page, size);
        List<CompanySearchResponse> content = result.content().stream()
                .map(CompanySearchResponse::from)
                .toList();
        return PageResponse.of(content, result.totalElements(), page, size);
    }

    @Operation(summary = "企業詳細取得（A-102）", description = "証券コードで企業の基本情報を取得する")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "取得成功"),
            @ApiResponse(
                    responseCode = "404",
                    description = "指定した証券コードの企業が存在しない",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    @GetMapping("/{securityCode}")
    public CompanyDetailResponse getCompany(@PathVariable String securityCode) {
        return CompanyDetailResponse.from(companyQueryUseCase.getCompanyDetail(securityCode));
    }
}
