package com.example.cash_ratio_analyzer_test.infrastructure.repository;

import com.example.cash_ratio_analyzer_test.domain.model.Company;
import com.example.cash_ratio_analyzer_test.domain.repository.ICompanyRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class InMemoryCompanyRepository implements ICompanyRepository {
    @Override
    public List<Company> findAll() {
        return null;
    }

    @Override
    public Company findByCompanyId(String companyId) {
        return null;
    }

    @Override
    public void save(Company company) {

    }
}
