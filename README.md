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

# 3. バックエンドを起動
cd backend
./mvnw spring-boot:run
```

### Frontend（実装予定）

---

## ドキュメント

- [要件定義](docs/requirements.md)
- [ER図](docs/er_diagram.md)

