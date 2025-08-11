package com.example.financialdataapp.infrastructure.database.jpa.entity;

import com.example.financialdataapp.domain.enums.Balance;
import com.example.financialdataapp.domain.enums.PeriodType;
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
    private PeriodType periodType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Balance balance;

    @Column(nullable = false)
    private Integer depth;

    public AccountMasterEntity() {}

    public AccountMasterEntity(String code, String nameJp, String nameEn, PeriodType periodType, Balance balance, Integer depth) {
        this.code = code;
        this.nameJp = nameJp;
        this.nameEn = nameEn;
        this.periodType = periodType;
        this.balance = balance;
        this.depth = depth;
    }
}
