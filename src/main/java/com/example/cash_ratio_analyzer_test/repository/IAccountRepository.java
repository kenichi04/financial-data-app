package com.example.cash_ratio_analyzer_test.repository;

import com.example.cash_ratio_analyzer_test.entity.Account;

import java.util.List;

public interface IAccountRepository {
    List<Account> findAll();
}
