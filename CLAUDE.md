# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

EDINET API（金融庁の電子開示システム）から有価証券報告書を取得・蓄積し、財務指標を算出・可視化するWebアプリ。特に小型株の現金比率など、ニッチな切り口の分析に強みを持つ投資家向けサービス。

- **Backend**: Spring Boot (Java 17) — 実装中
- **Frontend**: Next.js (TypeScript) — 実装予定
- **DB**: PostgreSQL 15

## Commands

All backend commands run from the `backend/` directory.

```bash
# DB起動（ルートディレクトリで実行）
docker-compose up -d

# アプリ起動
cd backend && ./mvnw spring-boot:run

# ビルド
./mvnw package

# テスト（全件）
./mvnw test

# テスト（単一クラス）
./mvnw test -Dtest=FinancialDocumentServiceTest

# テスト（単一メソッド）
./mvnw test -Dtest=FinancialDocumentServiceTest#createFinancialDocument

# jOOQコード生成（DB起動中かつ~/.m2/settings.xmlにDB接続情報が必要）
./mvnw generate-sources
```

## Environment Variables

アプリ起動には以下の環境変数が必要（`.env.example` を `.env` にコピーして設定）:

| 変数名 | 用途 |
|---|---|
| `DB_URL` | PostgreSQL接続URL（例: `jdbc:postgresql://localhost:5432/yourdb`） |
| `DB_USER` | DBユーザー名 |
| `DB_PASSWORD` | DBパスワード |
| `EDINET_API_SUBSCRIPTION_KEY` | EDINET API サブスクリプションキー |
| `DOWNLOAD_USER_DIR` | ダウンロードファイルの作業ディレクトリ |
| `POSTGRES_USER` / `POSTGRES_PASSWORD` / `POSTGRES_DB` | Docker Compose用PostgreSQL設定 |

jOOQコード生成時のDB接続情報は `~/.m2/settings.xml` に定義（`${db.url}`, `${db.user}`, `${db.password}` プロパティ）。

## Architecture

DDD（ドメイン駆動設計）の4層構造：

```
presentation/    ← REST Controllers（@RestController）
application/     ← UseCase・Service・DTO・enums
domain/          ← ドメインモデル・Repositoryインタフェース・enums
infrastructure/  ← DB実装（JPA / jOOQ）・InMemory実装
```

### 重要な設計方針

**書き込み（更新系）はJPA/Hibernate、読み取り（参照系）はjOOQ**を使い分ける。

- `IDocumentMetadataRepository` などのインタフェースをドメイン層に定義
- JPA実装（`DocumentMetadataRepository`）とInMemory実装（`InMemoryDocumentMetadataRepository`）が存在
- クエリサービス（`IDocumentMetadataQueryService` など）はjOOQで実装（`JooqDocumentMetadataQueryService`）

**UseCaseがサービスを束ねるオーケストレーター**として機能：
- `FinancialDocumentFetchUseCase` → EDINET APIからXBRL取得 → ファイル展開 → XBRL解析 → DB保存のフローを調整
- `DocumentMetadataFetchUseCase` → EDINET APIから書類一覧取得 → メタデータ保存

### EDINET連携フロー

1. `POST /api/edinet/metadata/fetch-and-save?fromDate=YYYY-MM-DD` → 指定日以降の書類一覧を取得してメタデータDBに保存
2. `POST /api/edinet/{documentId}/fetch-and-save` → 書類IDのXBRLデータを取得・解析してDB保存

XBRLファイルはZIP形式で取得 → `EdinetFileExtractionService` で展開 → `XbrlHeaderInfoExtractor`（ヘッダー情報）と `XbrlFinancialDataExtractor`（財務数値）でパース。

### DBマイグレーション

Flywayで管理。`backend/src/main/resources/db/migration/` 配下の `V{n}__*.sql` ファイルが順番に実行される。

### jOOQコード生成

DBスキーマから型安全なクエリクラスを生成。生成物は `target/generated-sources/jooq/` に出力。コード生成にはDBが起動済みであること、かつFlywayマイグレーション適用済みであることが必要。

## Key Domain Concepts

- **DocumentId / EdinetCode**: 値オブジェクト（`domain/model/`）
- **FinancialDocument**: 財務書類（documentId, edinetCode, documentType, fiscalYearEndDate, currency, 財務データリスト）
- **FinancialData**: 個別の財務数値（勘定科目コード、金額、期間種別など）
- **AccountMaster**: 勘定科目マスタ（初期データはV7マイグレーションで投入）
- **EdinetContext**: XBRLのコンテキスト情報（連結/個別、期間種別など）

## API Documentation

アプリ起動後、Swagger UIで確認可能: `http://localhost:8080/swagger-ui.html`
