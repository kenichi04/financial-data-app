package com.example.financialdataapp.domain.model;

public class Company {
    private Long id;
    private final EdinetCode edinetCode;
    private final String name;
    /** 証券コード */
    private final String securityCode;
    /** 法人番号 */
    private final String corporateNumber;

    public Company(EdinetCode edinetCode, String name, String securityCode, String corporateNumber) {
        this.edinetCode = edinetCode;
        this.name = name;
        this.securityCode = securityCode;
        this.corporateNumber = corporateNumber;
    }

    public EdinetCode getEdinetCode() {
        return edinetCode;
    }

    public String getName() {
        return name;
    }

    public String getSecurityCode() {
        return securityCode;
    }

    public String getCorporateNumber() {
        return corporateNumber;
    }
}
