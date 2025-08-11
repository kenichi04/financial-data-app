package com.example.financialdataapp.infrastructure.database.jpa.repository;

import com.example.financialdataapp.infrastructure.database.jpa.entity.AccountMasterEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountMasterRepository extends JpaRepository<AccountMasterEntity, Long> {
}
