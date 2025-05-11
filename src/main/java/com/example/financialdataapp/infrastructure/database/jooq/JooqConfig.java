package com.example.financialdataapp.infrastructure.database.jooq;

import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class JooqConfig {
    @Bean
    public DSLContext dslContext(DataSource datasource) {
        return DSL.using(datasource, SQLDialect.H2);
    }
}
