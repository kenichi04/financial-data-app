package com.example.cash_ratio_analyzer_test.domain.repository;

import com.example.cash_ratio_analyzer_test.domain.model.AccountMaster;

import java.util.List;

public interface IAccountRepository {
    List<AccountMaster> findAll();
}
