package com.example.cash_ratio_analyzer_test.domain.repository;

import com.example.cash_ratio_analyzer_test.domain.model.Account;

import java.util.List;

public interface IAccountRepository {
    List<Account> findAll();
}
