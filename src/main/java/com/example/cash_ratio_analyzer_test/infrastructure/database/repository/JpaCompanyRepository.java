package com.example.cash_ratio_analyzer_test.infrastructure.database.repository;

import com.example.cash_ratio_analyzer_test.domain.model.Company;
import com.example.cash_ratio_analyzer_test.domain.model.EdinetCode;
import com.example.cash_ratio_analyzer_test.domain.repository.ICompanyRepository;
import com.example.cash_ratio_analyzer_test.infrastructure.database.entity.CompanyEntity;
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
    public Company findByCompanyEdinetCode(String edinetCode) {
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
