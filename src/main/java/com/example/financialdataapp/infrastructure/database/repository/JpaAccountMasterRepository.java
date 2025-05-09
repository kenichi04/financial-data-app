package com.example.financialdataapp.infrastructure.database.repository;

import com.example.financialdataapp.domain.model.AccountMaster;
import com.example.financialdataapp.domain.repository.IAccountMasterRepository;
import com.example.financialdataapp.infrastructure.database.entity.AccountMasterEntity;
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
        return entities.stream().map(entity -> toModel(entity)).toList();
    }

    protected AccountMaster toModel(AccountMasterEntity from) {
        return new AccountMaster(
                from.getId(),
                from.getCode(),
                from.getNameJp(),
                from.getNameEn(),
                from.getBalance());
    }

    protected AccountMasterEntity toEntity(AccountMaster from) {
        var entity = new AccountMasterEntity();
        entity.setId(from.getId());
        entity.setCode(from.getCode());
        entity.setNameJp(from.getNameJp());
        entity.setNameEn(from.getNameEn());
        entity.setBalance(from.getBalance());
        return entity;
    }
}
