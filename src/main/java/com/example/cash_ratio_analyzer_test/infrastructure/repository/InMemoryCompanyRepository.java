package com.example.cash_ratio_analyzer_test.infrastructure.repository;

import com.example.cash_ratio_analyzer_test.domain.model.Company;
import com.example.cash_ratio_analyzer_test.domain.repository.ICompanyRepository;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class InMemoryCompanyRepository implements ICompanyRepository {

    private Map<Long, Company> companyStore = new HashMap<>();

    @Override
    public List<Company> findAll() {
        return List.copyOf(companyStore.values());
    }

    @Override
    public Company findByCompanyId(long companyId) {
        return companyStore.getOrDefault(companyId, null);
    }

    @Override
    public void save(Company company) {
        companyStore.put(company.getCompanyId(), company);
    }
}
