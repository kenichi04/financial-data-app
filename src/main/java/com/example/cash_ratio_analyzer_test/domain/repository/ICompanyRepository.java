package com.example.cash_ratio_analyzer_test.domain.repository;

import com.example.cash_ratio_analyzer_test.domain.model.Company;

import java.util.List;

public interface ICompanyRepository {
    List<Company> findAll();
    Company findByEdinetCode(String edinetCode);
    void save(Company company);
    void save(List<Company> companies);
}
