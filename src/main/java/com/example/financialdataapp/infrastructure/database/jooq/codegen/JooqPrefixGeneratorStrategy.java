package com.example.financialdataapp.infrastructure.database.jooq.codegen;

import org.jooq.codegen.DefaultGeneratorStrategy;
import org.jooq.meta.Definition;

public class JooqPrefixGeneratorStrategy extends DefaultGeneratorStrategy {
    @Override
    public String getJavaClassName(Definition definition, Mode mode) {
        return "Jooq" + super.getJavaClassName(definition, mode);
    }
}
