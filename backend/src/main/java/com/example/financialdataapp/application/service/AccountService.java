package com.example.financialdataapp.application.service;

import com.example.financialdataapp.domain.model.AccountMaster;
import com.example.financialdataapp.domain.repository.IAccountMasterRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountService {

    private final IAccountMasterRepository accountMasterRepository;

    public AccountService(IAccountMasterRepository accountMasterRepository) {
        this.accountMasterRepository = accountMasterRepository;
    }

    public List<AccountMaster> getAccounts() {
        return accountMasterRepository.findAll();
    }
}
