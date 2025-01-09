package com.example.cash_ratio_analyzer_test.repository;

import com.example.cash_ratio_analyzer_test.entity.Company;
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
}
