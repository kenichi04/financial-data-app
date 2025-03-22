package com.example.cash_ratio_analyzer.domain.repository;

import com.example.cash_ratio_analyzer.domain.model.AccountMaster;

import java.util.List;

public interface IAccountMasterRepository {
    List<AccountMaster> findAll();
}
