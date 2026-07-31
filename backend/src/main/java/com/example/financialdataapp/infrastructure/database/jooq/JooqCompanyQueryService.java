package com.example.financialdataapp.infrastructure.database.jooq;

import com.example.financialdataapp.application.service.dto.CompanyDetailDto;
import com.example.financialdataapp.application.service.dto.CompanyDto;
import com.example.financialdataapp.application.service.dto.CompanySearchDto;
import com.example.financialdataapp.application.service.dto.PagedResult;
import com.example.financialdataapp.application.service.metadata.ICompanyQueryService;
import com.example.financialdataapp.application.service.metadata.SecurityCodeNormalizer;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Table;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static com.example.financialdataapp.tables.Company.COMPANY;
import static com.example.financialdataapp.tables.FinancialMetrics.FINANCIAL_METRICS;

@Repository
public class JooqCompanyQueryService implements ICompanyQueryService {

    private final DSLContext create;

    public JooqCompanyQueryService(DSLContext create) {
        this.create = create;
    }

    @Override
    public List<CompanyDto> fetchAllCompanies() {
        return create
                .select(
                        COMPANY.EDINET_CODE,
                        COMPANY.NAME,
                        COMPANY.SECURITY_CODE,
                        COMPANY.CORPORATE_NUMBER
                )
                .from(COMPANY)
                .fetch()
                .map(record -> new CompanyDto(
                        record.get(COMPANY.EDINET_CODE),
                        record.get(COMPANY.NAME),
                        record.get(COMPANY.SECURITY_CODE),
                        record.get(COMPANY.CORPORATE_NUMBER)
                ));
    }

    @Override
    public PagedResult<CompanySearchDto> searchCompanies(String keyword, int offset, int limit) {
        Condition condition = keywordCondition(keyword);
        int totalElements = create.fetchCount(COMPANY, condition);
        if (totalElements == 0) {
            return new PagedResult<>(List.of(), 0);
        }

        Table<?> latestMetrics = LatestFinancialMetrics.asTable(create);
        List<CompanySearchDto> content = create
                .select(
                        COMPANY.SECURITY_CODE,
                        COMPANY.EDINET_CODE,
                        COMPANY.NAME,
                        latestMetrics.field(FINANCIAL_METRICS.FISCAL_YEAR_END_DATE),
                        latestMetrics.field(FINANCIAL_METRICS.ROE),
                        latestMetrics.field(FINANCIAL_METRICS.ROA),
                        latestMetrics.field(FINANCIAL_METRICS.CASH_RATIO)
                )
                .from(COMPANY)
                .leftJoin(latestMetrics)
                .on(latestMetrics.field(FINANCIAL_METRICS.EDINET_CODE).eq(COMPANY.EDINET_CODE))
                .where(condition)
                // ページ間で並びがぶれないよう一意なキーで並べる
                .orderBy(COMPANY.SECURITY_CODE)
                .offset(offset)
                .limit(limit)
                .fetch()
                .map(record -> new CompanySearchDto(
                        SecurityCodeNormalizer.toMarketCode(record.get(COMPANY.SECURITY_CODE)),
                        record.get(COMPANY.EDINET_CODE),
                        record.get(COMPANY.NAME),
                        record.get(latestMetrics.field(FINANCIAL_METRICS.FISCAL_YEAR_END_DATE)),
                        record.get(latestMetrics.field(FINANCIAL_METRICS.ROE)),
                        record.get(latestMetrics.field(FINANCIAL_METRICS.ROA)),
                        record.get(latestMetrics.field(FINANCIAL_METRICS.CASH_RATIO))
                ));

        return new PagedResult<>(content, totalElements);
    }

    @Override
    public Optional<CompanyDetailDto> findBySecurityCode(String securityCode) {
        Table<?> latestMetrics = LatestFinancialMetrics.asTable(create);
        Record record = create
                .select(
                        COMPANY.SECURITY_CODE,
                        COMPANY.EDINET_CODE,
                        COMPANY.NAME,
                        COMPANY.CORPORATE_NUMBER,
                        latestMetrics.field(FINANCIAL_METRICS.FISCAL_YEAR_END_DATE)
                )
                .from(COMPANY)
                .leftJoin(latestMetrics)
                .on(latestMetrics.field(FINANCIAL_METRICS.EDINET_CODE).eq(COMPANY.EDINET_CODE))
                .where(COMPANY.SECURITY_CODE.startsWith(securityCode))
                .orderBy(COMPANY.SECURITY_CODE)
                .limit(1)
                .fetchOne();

        if (record == null) {
            return Optional.empty();
        }
        return Optional.of(new CompanyDetailDto(
                SecurityCodeNormalizer.toMarketCode(record.get(COMPANY.SECURITY_CODE)),
                record.get(COMPANY.EDINET_CODE),
                record.get(COMPANY.NAME),
                record.get(COMPANY.CORPORATE_NUMBER),
                record.get(latestMetrics.field(FINANCIAL_METRICS.FISCAL_YEAR_END_DATE))
        ));
    }

    /**
     * 企業名の部分一致（大文字小文字を無視）、または証券コードの前方一致。
     * 証券コードはDBに5桁（末尾0付き）で保存されているため、4桁指定を前方一致で吸収する。
     * jOOQの {@code containsIgnoreCase} / {@code startsWith} は {@code %} や {@code _} をエスケープする。
     */
    private Condition keywordCondition(String keyword) {
        return COMPANY.NAME.containsIgnoreCase(keyword)
                .or(COMPANY.SECURITY_CODE.startsWith(keyword));
    }
}
