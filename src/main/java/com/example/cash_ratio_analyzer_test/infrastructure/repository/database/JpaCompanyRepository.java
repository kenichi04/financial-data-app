package com.example.cash_ratio_analyzer_test.infrastructure.repository.database;

import com.example.cash_ratio_analyzer_test.domain.model.Company;
import com.example.cash_ratio_analyzer_test.domain.repository.ICompanyRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Primary
public class JpaCompanyRepository implements ICompanyRepository {

    private final CompanyRepository companyRepository;

    public JpaCompanyRepository(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    @Override
    public List<Company> findAll() {
        return null;
    }

    @Override
    public Company findByCompanyEdinetCode(String edinetCode) {
        return null;
    }

    @Override
    public void save(Company company) {

    }

    @Override
    public void save(List<Company> companies) {

    }
}
