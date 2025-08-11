package com.example.financialdataapp.infrastructure.inmemory;

import com.example.financialdataapp.domain.enums.Balance;
import com.example.financialdataapp.domain.enums.PeriodType;
import com.example.financialdataapp.domain.model.AccountMaster;
import com.example.financialdataapp.domain.repository.IAccountMasterRepository;
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
            put(1L, new AccountMaster("CashAndDeposits", "現金預金", "Cash and deposits", PeriodType.INSTANT, Balance.DEBIT, 4));
        }
    };

    @Override
    public List<AccountMaster> findAll() {
        return new ArrayList<>(accountStore.values());
    }
}
