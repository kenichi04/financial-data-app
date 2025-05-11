package com.example.financialdataapp.infrastructure.database.jpa.repository;

import com.example.financialdataapp.infrastructure.database.jpa.entity.CompanyEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyRepository extends JpaRepository<CompanyEntity, Long> {
    CompanyEntity findByEdinetCode(String edinetCode);
}
