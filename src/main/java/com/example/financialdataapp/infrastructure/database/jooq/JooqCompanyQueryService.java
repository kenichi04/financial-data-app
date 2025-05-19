package com.example.financialdataapp.infrastructure.database.jooq;

import com.example.financialdataapp.application.service.dto.CompanyDto;
import com.example.financialdataapp.application.service.metadata.ICompanyQueryService;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.example.financialdataapp.tables.JooqCompany.COMPANY;

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
}
