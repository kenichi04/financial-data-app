# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

EDINET API（金融庁の電子開示システム）から有価証券報告書を取得・蓄積し、財務指標を算出・可視化するWebアプリ。特に小型株の現金比率など、ニッチな切り口の分析に強みを持つ投資家向けサービス。

- **Backend**: Spring Boot 3.4 (Java 17) — 実装中
- **Frontend**: Next.js (TypeScript) — 実装予定
- **DB**: PostgreSQL 15

## Commands

All backend commands run from the `backend/` directory.

```bash
# DB起動（ルートディレクトリで実行）
docker-compose up -d

# アプリ起動
./mvnw spring-boot:run

# ビルド
./mvnw package

# テスト（全件）
./mvnw test

# テスト（単一クラス）
./mvnw test -Dtest=FinancialDocumentServiceTest

# テスト（単一メソッド）
./mvnw test -Dtest=FinancialDocumentServiceTest#createFinancialDocument
```

jOOQのクエリクラスはビルド時にマイグレーションSQLから自動生成される（DB接続不要）。手動の生成コマンドはない。

## Environment Variables

アプリ起動には以下の環境変数が必要（`.env.example` を `.env` にコピーして設定）:

| 変数名 | 用途 |
|---|---|
| `DB_URL` | PostgreSQL接続URL（例: `jdbc:postgresql://localhost:5432/yourdb`） |
| `DB_USER` | DBユーザー名 |
| `DB_PASSWORD` | DBパスワード |
| `EDINET_API_SUBSCRIPTION_KEY` | EDINET API サブスクリプションキー |
| `EDINET_API_DOCUMENT_LIST_URL` | EDINET API 書類一覧取得URL（例: `https://api.edinet-fsa.go.jp/api/v2/documents.json?date={date}&type={type}&Subscription-Key={key}`） |
| `EDINET_API_DOCUMENT_RETRIEVAL_URL` | EDINET API 書類取得URL（例: `https://api.edinet-fsa.go.jp/api/v2/documents/{docNumber}?type={type}&Subscription-Key={key}`） |
| `DOWNLOAD_USER_DIR` | ダウンロードファイルの作業ディレクトリ |
| `POSTGRES_USER` / `POSTGRES_PASSWORD` / `POSTGRES_DB` | Docker Compose用PostgreSQL設定 |

`backend/pom.xml` の `db.url` / `db.user` / `db.password` プロパティはflyway-maven-plugin（`./mvnw flyway:migrate`）用のデフォルト接続情報（ローカルdocker-compose用）。異なる値を使う場合は `-Ddb.url=...` で上書きする。

## Architecture

DDD（ドメイン駆動設計）の4層構造：

```
presentation/    ← REST Controllers（@RestController）
application/     ← UseCase・Service・DTO・enums・constants
domain/          ← ドメインモデル・Repositoryインタフェース・enums
infrastructure/  ← DB実装（JPA / jOOQ）・InMemory実装
```

### 重要な設計方針

**書き込み（更新系）はJPA/Hibernate、読み取り（参照系）はjOOQ**を使い分ける。

インフラ層の命名規則：
- `JpaXxxRepository`（`JpaDocumentMetadataRepository` など）— Spring Data JPA の `JpaRepository` を継承したインタフェース。直接は使わない。
- `XxxRepository`（`DocumentMetadataRepository` など）— ドメイン層のリポジトリインタフェース（`IDocumentMetadataRepository`）の JPA 実装。内部で `JpaXxxRepository` を使う。
- `JooqXxxQueryService`（`JooqDocumentMetadataQueryService` など）— 読み取り専用。アプリ層のクエリサービスインタフェース（`IDocumentMetadataQueryService`）の jOOQ 実装。
- `InMemoryXxxRepository` — テスト・開発用のインメモリ実装。

**UseCaseがサービスを束ねるオーケストレーター**として機能：
- `FinancialDocumentFetchUseCase` → EDINET APIからXBRL取得 → ファイル展開 → XBRL解析 → DB保存
- `DocumentMetadataFetchUseCase` → EDINET APIから書類一覧取得 → メタデータ保存
- `FinancialDocumentQueryUseCase` / `DocumentMetadataQueryUseCase` → 読み取り専用（jOOQ経由）

### REST API エンドポイント

| メソッド | パス | 説明 |
|---|---|---|
| POST | `/api/edinet/metadata/fetch-and-save?fromDate=YYYY-MM-DD` | 指定日以降の書類一覧を取得・保存 |
| POST | `/api/edinet/{documentId}/fetch-and-save` | 書類IDのXBRLデータを取得・解析・保存 |
| GET | `/api/document-metadata/unprocessedMetadata` | 未処理メタデータ一覧（jOOQ） |
| GET | `/api/document-metadata/companies` | 企業一覧（jOOQ） |
| GET | `/api/financial-documents/{documentId}` | 財務書類取得（jOOQ） |

`/v1/` プレフィックスの付いたエンドポイントと `EdinetOutputController`（`/edinet/output/`）は `@Deprecated`。

### EDINET連携フロー

XBRLファイルはZIP形式で取得 → `EdinetFileExtractionService` で展開 → `XbrlHeaderInfoExtractor`（ヘッダー情報）と `XbrlFinancialDataExtractor`（財務数値）でパース。
XBRLの名前空間定数は `application/service/constants/XbrlConstants.java` に集約。

### DBマイグレーション

Flywayで管理。`backend/src/main/resources/db/migration/` 配下の `V{n}__*.sql` ファイルが順番に実行される。

### jOOQコード生成

FlywayのマイグレーションSQLから型安全なクエリクラスを**ビルド時に自動生成**する（jOOQのDDLDatabase使用。DB接続不要）。生成物は `backend/target/generated-sources/jooq/` に出力され、Git管理しない。マイグレーションを追加・変更すれば次のビルドで自動反映されるため、手動の再生成手順はない。ただしDDLDatabaseはjOOQのSQLパーサーでマイグレーションを解釈するため、**PostgreSQL固有の構文（トリガー・PL/pgSQL関数等）は解釈できない**場合がある（対処法・経緯は `docs/jooq_codegen_improvement.md`）。

## Key Domain Concepts

- **DocumentId / EdinetCode**: 値オブジェクト（`domain/model/`）
- **FinancialDocument**: 財務書類（documentId, edinetCode, documentType, fiscalYearEndDate, currency, 財務データリスト）
- **FinancialData**: 個別の財務数値（勘定科目コード、金額、期間種別など）
- **AccountMaster**: 勘定科目マスタ（初期データはV7マイグレーションで投入）
- **EdinetContext**: XBRLのコンテキスト情報（連結/個別、期間種別など）。`domain/enums/context/` 配下の `ConsolidatedType`・`YearType`・`PeriodUnit` の組み合わせで表現。

## Testing

テストは `@ExtendWith(MockitoExtension.class)` による純粋ユニットテスト（Spring Context不使用）。インフラ層はモック化し、`InMemoryXxxRepository` をテスト用スタブとして使用する。

## API Documentation

アプリ起動後、Swagger UIで確認可能: `http://localhost:8080/swagger-ui.html`

## Design Documents

新機能の実装前に `docs/requirements.md` を参照。機能ごとのフェーズ（MVP / Phase 2 / 将来）と優先度が定義されており、実装対象の判断基準になる。
