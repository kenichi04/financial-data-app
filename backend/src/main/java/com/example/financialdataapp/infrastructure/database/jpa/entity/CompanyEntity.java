package com.example.financialdataapp.infrastructure.database.jpa.entity;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Table(name = "company")
@Getter
public class CompanyEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String edinetCode;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String securityCode;

    @Column(nullable = false, unique = true)
    private String corporateNumber;

    public CompanyEntity() {}

    public CompanyEntity(String edinetCode, String name, String securityCode, String corporateNumber) {
        this.edinetCode = edinetCode;
        this.name = name;
        this.securityCode = securityCode;
        this.corporateNumber = corporateNumber;
    }

}
