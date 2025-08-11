package com.example.financialdataapp.infrastructure.database.jpa.repository;

import com.example.financialdataapp.domain.model.Company;
import com.example.financialdataapp.domain.model.EdinetCode;
import com.example.financialdataapp.domain.repository.ICompanyRepository;
import com.example.financialdataapp.infrastructure.database.jpa.entity.CompanyEntity;
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
        var entities = companyRepository.findAll();
        return entities.stream().map(this::toModel).toList();
    }

    @Override
    public Company findByEdinetCode(String edinetCode) {
        var entity = companyRepository.findByEdinetCode(edinetCode);
        return toModel(entity);
    }

    @Override
    public void save(Company company) {
        var entity = toEntity(company);
        companyRepository.save(entity);
    }

    @Override
    public void save(List<Company> companies) {
        var entities = companies.stream().map(this::toEntity).toList();
        companyRepository.saveAll(entities);
    }

    private Company toModel(CompanyEntity from) {
        if (from == null) return null;

        return new Company(
                new EdinetCode(from.getEdinetCode()),
                from.getName(),
                from.getSecurityCode(),
                from.getCorporateNumber());
    }

    private CompanyEntity toEntity(Company from) {
        return new CompanyEntity(
                from.getEdinetCode().toString(),
                from.getName(),
                from.getSecurityCode(),
                from.getCorporateNumber());
    }
}
