# Financial Data App

Spring Bootを使用した財務データアプリケーション。EDINETから財務データを取得し、分析するためのバックエンドアプリケーションです。

## 技術スタック

- **Java**: 17
- **フレームワーク**: Spring Boot 3.4.0
- **ビルドツール**: Maven
- **データベース**: PostgreSQL
- **マイグレーション**: Flyway
- **ORM/SQL**: JOOQ, Spring Data JPA
- **テスト**: JUnit 5, Mockito

## GitHub Actions CI/CD

このプロジェクトでは、GitHub Actionsを使用してユニットテストの自動実行を行っています。

### ワークフロー

- **ファイル**: `.github/workflows/test.yml`
- **トリガー**:
  - `main` または `develop` ブランチへのプッシュ
  - `main` または `develop` ブランチへのプルリクエスト

### CI処理フロー

1. **環境セットアップ**
   - Ubuntu Latest ランナーを使用
   - PostgreSQL 16 をサービスコンテナとして起動
   - JDK 17 (Temurin) をセットアップ
   - Mavenキャッシュを有効化

2. **データベースマイグレーション**
   - Flywayを使用してテスト用データベースのスキーマを作成

3. **コード生成**
   - JOOQを使用してデータベーススキーマからJavaコードを生成

4. **テスト実行**
   - Mavenを使用してすべてのユニットテストを実行

## ローカル開発

### 前提条件

- Java 17
- PostgreSQL 16以上
- Maven (またはプロジェクトに含まれるMaven Wrapper)

### セットアップ

1. リポジトリをクローン
```bash
git clone https://github.com/kenichi04/financial-data-app.git
cd financial-data-app/backend
```

2. PostgreSQLデータベースを作成
```bash
createdb financial_data_app
```

3. Flywayマイグレーションを実行
```bash
./mvnw flyway:migrate -Ddb.url=jdbc:postgresql://localhost:5432/financial_data_app -Ddb.user=<your_user> -Ddb.password=<your_password>
```

4. JOOQコードを生成
```bash
./mvnw generate-sources -Ddb.url=jdbc:postgresql://localhost:5432/financial_data_app -Ddb.user=<your_user> -Ddb.password=<your_password>
```

5. テストを実行
```bash
./mvnw test
```

## ライセンス

このプロジェクトのライセンスについては、プロジェクトオーナーに確認してください。
