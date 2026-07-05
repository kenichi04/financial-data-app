# 財務分析Webアプリ

EDINET APIから有価証券報告書を取得・蓄積し、財務指標を算出・可視化するWebアプリ開発中。

---

## 技術スタック

### Backend（実装中）

| 区分 | 技術 |
|---|---|
| フレームワーク | Spring Boot (Java) |
| ORM（更新系） | Hibernate |
| SQLビルダー（参照系） | jOOQ |
| DB | PostgreSQL 15 |
| DBマイグレーション | Flyway |

### Frontend（実装予定）

> Next.js (TypeScript) で実装予定

### インフラ

※一旦DBセットアップのみ

| 区分 | 技術 |
|---|---|
| コンテナ | Docker |

---

## ディレクトリ構成

```
financial-data-app/
├── backend/                        # Spring Boot アプリ
│   └── src/main/java/.../
│       ├── presentation/           # Controller（REST API）
│       ├── application/            # UseCase・Service・DTO
│       ├── domain/                 # ドメインモデル・Repository インタフェース
│       └── infrastructure/         # DB実装（JPA / jOOQ）
├── docs/                           # 設計ドキュメント
├── docker-compose.yml              # ローカル DB 起動用
└── .env.example                    # 環境変数テンプレート
```

---

## ローカル開発セットアップ

### 前提条件

- Java 17+
- Docker / Docker Compose

### Backend

```bash
# 1. 環境変数を設定
cp .env.example .env
# .env を編集して POSTGRES_USER / POSTGRES_PASSWORD / POSTGRES_DB を設定

# 2. DB を起動
docker-compose up -d

# 3. ~/.m2/settings.xml に DB 接続情報を設定（jOOQ コード生成で使用）
```

```xml
<!-- ~/.m2/settings.xml -->
<settings>
  <profiles>
    <profile>
      <id>local</id>
      <properties>
        <db.url>jdbc:postgresql://localhost:5432/yourdb</db.url>
        <db.user>your_db_user</db.user>
        <db.password>your_db_password</db.password>
      </properties>
    </profile>
  </profiles>
  <activeProfiles>
    <activeProfile>local</activeProfile>
  </activeProfiles>
</settings>
```

```bash
# 4. Flyway マイグレーションを適用
cd backend
./mvnw flyway:migrate

# 5. jOOQ コード生成（Flyway 適用後の DB スキーマを元に生成）
./mvnw generate-sources

# 6. アプリ本体の環境変数を設定（DB_URL / DB_USER / DB_PASSWORD / EDINET_API_SUBSCRIPTION_KEY /
#    DOWNLOAD_USER_DIR / EDINET_API_DOCUMENT_LIST_URL / EDINET_API_DOCUMENT_RETRIEVAL_URL）
#    → 各変数の説明は CLAUDE.md の Environment Variables 参照
#    IntelliJ 等の IDE で実行する場合は Run Configuration の環境変数に設定する

# 7. バックエンドを起動
./mvnw spring-boot:run
```

### Frontend（実装予定）

---
