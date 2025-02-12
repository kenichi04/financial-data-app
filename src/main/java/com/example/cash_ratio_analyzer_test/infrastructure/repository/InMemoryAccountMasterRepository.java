package com.example.cash_ratio_analyzer_test.infrastructure.repository;

import com.example.cash_ratio_analyzer_test.domain.enums.Balance;
import com.example.cash_ratio_analyzer_test.domain.model.AccountMaster;
import com.example.cash_ratio_analyzer_test.domain.repository.IAccountMasterRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class InMemoryAccountMasterRepository implements IAccountMasterRepository {

    private Map<Long, AccountMaster> accountStore = new HashMap<>(){
        {
            // 現金預金
            put(1L, new AccountMaster("CashAndDeposits", "現金預金", "Cash and deposits", Balance.DEBIT));
        }
    };

    @Override
    public List<AccountMaster> findAll() {
        return new ArrayList<>(accountStore.values());
    }
}
