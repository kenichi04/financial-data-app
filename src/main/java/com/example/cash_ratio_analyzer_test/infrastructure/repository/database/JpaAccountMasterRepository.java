package com.example.cash_ratio_analyzer_test.infrastructure.repository.database;

import com.example.cash_ratio_analyzer_test.domain.model.AccountMaster;
import com.example.cash_ratio_analyzer_test.domain.repository.IAccountMasterRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Primary
public class JpaAccountMasterRepository implements IAccountMasterRepository {

    private final AccountMasterRepository accountMasterRepository;

    public JpaAccountMasterRepository(AccountMasterRepository jpaAccountMasterRepository) {
        this.accountMasterRepository = jpaAccountMasterRepository;
    }

    @Override
    public List<AccountMaster> findAll() {
        var entities = accountMasterRepository.findAll();
        return entities.stream().map(entity -> new AccountMaster(entity.getCode(), entity.getNameJp(), entity.getNameEn(), entity.getBalance())).toList();
    }
}
