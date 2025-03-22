package com.example.cash_ratio_analyzer.infrastructure.database.entity;

import com.example.cash_ratio_analyzer.domain.enums.Balance;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "account_master")
@Getter
@Setter
public class AccountMasterEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String code;

    @Column(nullable = false)
    private String nameJp;

    @Column(nullable = false)
    private String nameEn;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Balance balance;

    public AccountMasterEntity() {}

    public AccountMasterEntity(String code, String nameJp, String nameEn, Balance balance) {
        this.code = code;
        this.nameJp = nameJp;
        this.nameEn = nameEn;
        this.balance = balance;
    }
}
