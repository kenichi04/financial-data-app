package com.example.cash_ratio_analyzer_test.infrastructure.repository.database;

import com.example.cash_ratio_analyzer_test.domain.model.AccountMaster;
import com.example.cash_ratio_analyzer_test.domain.repository.IAccountMasterRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Primary
public class AccountMasterRepository implements IAccountMasterRepository {

    private final JpaAccountMasterRepository jpaAccountMasterRepository;

    public AccountMasterRepository(JpaAccountMasterRepository jpaAccountMasterRepository) {
        this.jpaAccountMasterRepository = jpaAccountMasterRepository;
    }

    @Override
    public List<AccountMaster> findAll() {
        var entities = jpaAccountMasterRepository.findAll();
        return entities.stream().map(entity -> new AccountMaster(entity.getCode(), entity.getNameJp(), entity.getNameEn(), entity.getBalance())).toList();
    }
}
