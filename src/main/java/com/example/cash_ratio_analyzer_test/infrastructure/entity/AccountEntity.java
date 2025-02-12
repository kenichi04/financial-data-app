package com.example.cash_ratio_analyzer_test.infrastructure.entity;

import com.example.cash_ratio_analyzer_test.domain.enums.Balance;
import jakarta.persistence.*;

@Entity
@Table(name = "account")
public class AccountEntity {

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

    public AccountEntity() {}

    public AccountEntity(String code, String nameJp, String nameEn, Balance balance) {
        this.code = code;
        this.nameJp = nameJp;
        this.nameEn = nameEn;
        this.balance = balance;
    }
}
