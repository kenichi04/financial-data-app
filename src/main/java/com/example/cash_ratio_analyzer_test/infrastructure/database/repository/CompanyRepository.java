package com.example.cash_ratio_analyzer_test.infrastructure.database.repository;

import com.example.cash_ratio_analyzer_test.infrastructure.database.entity.CompanyEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyRepository extends JpaRepository<CompanyEntity, Long> {
    CompanyEntity findByEdinetCode(String edinetCode);
}
