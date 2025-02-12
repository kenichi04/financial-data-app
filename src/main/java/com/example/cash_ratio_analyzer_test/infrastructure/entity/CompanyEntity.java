package com.example.cash_ratio_analyzer_test.infrastructure.entity;

import jakarta.persistence.*;

// 永続化用のEntityクラス
@Entity
@Table(name = "company")
public class CompanyEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String edinetCode;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String securityCode;

    @Column(nullable = false)
    private String corporateNumber;

    public CompanyEntity() {
    }

    public CompanyEntity(String edinetCode, String name, String securityCode, String corporateNumber) {
        this.edinetCode = edinetCode;
        this.name = name;
        this.securityCode = securityCode;
        this.corporateNumber = corporateNumber;
    }

}
