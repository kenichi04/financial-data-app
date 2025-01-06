package com.example.cash_ratio_analyzer_test.repository;

import com.example.cash_ratio_analyzer_test.entity.Account;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class InMemoryAccountRepository implements IAccountRepository {

    private Map<Long, Account> accountStore = new HashMap<>(){
        {
            // 現金預金
            put(1L, new Account("test1", "CashAndDeposits"));
        }
    };

    @Override
    public List<Account> findAll() {
        return new ArrayList<>(accountStore.values());
    }
}
