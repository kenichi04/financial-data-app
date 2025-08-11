package com.example.financialdataapp.domain.repository;

import com.example.financialdataapp.domain.model.Company;

import java.util.List;

public interface ICompanyRepository {
    List<Company> findAll();
    Company findByEdinetCode(String edinetCode);
    void save(Company company);
    void save(List<Company> companies);
}
