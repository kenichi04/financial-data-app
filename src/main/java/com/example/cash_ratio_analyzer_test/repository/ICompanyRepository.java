package com.example.cash_ratio_analyzer_test.repository;

import com.example.cash_ratio_analyzer_test.entity.Company;

import java.util.List;

public interface ICompanyRepository {
    List<Company> findAll();
    Company findByCompanyId(String companyId);
}
