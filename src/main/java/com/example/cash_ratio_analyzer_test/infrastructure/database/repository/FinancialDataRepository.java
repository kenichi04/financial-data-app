package com.example.cash_ratio_analyzer_test.infrastructure.database.repository;

import com.example.cash_ratio_analyzer_test.infrastructure.database.entity.FinancialDataEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FinancialDataRepository extends JpaRepository<FinancialDataEntity, Long> {
}
