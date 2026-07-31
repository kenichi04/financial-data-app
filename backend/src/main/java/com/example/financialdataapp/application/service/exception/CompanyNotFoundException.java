package com.example.financialdataapp.application.service.exception;

/**
 * 指定された企業が存在しない場合にスローされます。
 * プレゼンテーション層で 404 {@code COMPANY_NOT_FOUND} に変換されます。
 */
public class CompanyNotFoundException extends RuntimeException {

    public CompanyNotFoundException(String securityCode) {
        super("Company not found: " + securityCode);
    }
}
