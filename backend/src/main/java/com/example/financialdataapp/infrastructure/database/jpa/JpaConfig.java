package com.example.financialdataapp.infrastructure.database.jpa;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.Optional;

/**
 * JPAの設定クラス。
 *
 * <p>このクラスは、JPAの監査機能を有効化し、監査者情報を提供するための設定を行います。</p>
 * <p>監査者情報として、システムユーザーをデフォルトで使用します。</p>
 */
@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
public class JpaConfig {

    private static final String SYSTEM_USER = "system";

    @Bean
    public AuditorAware<String> auditorProvider() {
        // システムユーザーを監査者として設定
        // 基本的にデータ登録はアプリ側で行うため、ユーザー情報は取得しない
        return () -> Optional.of(SYSTEM_USER);
    }
}
