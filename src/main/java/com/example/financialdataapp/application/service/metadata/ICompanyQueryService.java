package com.example.financialdataapp.application.service.metadata;

import com.example.financialdataapp.application.service.dto.CompanyDto;

import java.util.List;

public interface ICompanyQueryService {
    List<CompanyDto> fetchAllCompanies();
}
