package com.example.financialdataapp.domain.repository;

import com.example.financialdataapp.domain.model.AccountMaster;

import java.util.List;

public interface IAccountMasterRepository {
    List<AccountMaster> findAll();
}
