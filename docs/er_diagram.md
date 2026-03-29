# ER図

最終更新: 2026-03-22（FlywayのSQLをベースに再作成）
Claude Code 作成

---

## テーブル一覧

### Phase 1（実装済み・実装予定）

| テーブル名 | 説明 |
|---|---|
| document_metadata | EDINET書類一覧APIの取得結果 |
| company | 企業マスタ |
| account_master | 勘定科目マスタ（EDINET 勘定科目リスト.xlsx準拠） |
| financial_document | 有価証券報告書メタ情報（EDINET書類取得APIの結果） |
| financial_data | 財務数値データ（1勘定科目1レコード） |

### Phase 2（スクリーニング・認証導入時に追加）

| テーブル名 | 説明 |
|---|---|
| financial_metrics | 計算済み財務指標（スクリーニング用キャッシュ） |
| users | ユーザー |
| subscriptions | サブスクリプション情報 |
| watchlists | ウォッチリスト |
| watchlist_items | ウォッチリスト内の銘柄 |

---

## Mermaid ER図

### Phase 1

```mermaid
erDiagram

    document_metadata {
        bigint      id                  PK  "GENERATED ALWAYS AS IDENTITY"
        varchar     document_id         UK  "EDINET書類ID"
        text        description             "書類概要"
        varchar     edinet_code             "EDINETコード"
        int         document_type           "EDINET書類種別コード（例: 120=有価証券報告書）"
        varchar     form_code               "様式コード"
        date        submission_date         "提出日"
        boolean     processed               "処理済みフラグ（default: false）"
        timestamp   created_at
        timestamp   updated_at
        varchar     created_by
        varchar     updated_by
    }

    company {
        bigint      id                  PK  "GENERATED ALWAYS AS IDENTITY"
        varchar     edinet_code         UK  "EDINETコード"
        varchar     name                    "企業名"
        varchar     security_code       UK  "証券コード（4桁）"
        varchar     corporate_number    UK  "法人番号"
    }

    account_master {
        bigint      id                  PK  "GENERATED ALWAYS AS IDENTITY"
        varchar     code                UK  "勘定科目コード"
        varchar     name_jp                 "勘定科目名（日本語）"
        varchar     name_en                 "勘定科目名（英語）"
        varchar     period_type             "期間種別: INSTANT / DURATION"
        varchar     balance                 "貸借区分: 借方 / 貸方 / NONE"
        int         depth                   "階層深度"
    }

    financial_document {
        bigint      id                  PK  "GENERATED ALWAYS AS IDENTITY"
        varchar     document_id         UK  "EDINET書類ID（document_metadataと対応、FK制約なし）"
        varchar     edinet_code             "EDINETコード（APIレスポンスの生値）"
        varchar     document_type           "EDINET様式種別（例: 第三号様式）"
        date        fiscal_year_end_date    "決算期末日"
        varchar     currency                "通貨"
        bigint      company_id          FK  "companyテーブルへの参照"
        timestamp   created_at
        timestamp   updated_at
        varchar     created_by
        varchar     updated_by
    }

    financial_data {
        bigint      id                      PK  "GENERATED ALWAYS AS IDENTITY"
        bigint      financial_document_id   FK
        bigint      account_id              FK  "account_masterへの参照"
        varchar     year_type                   "年度種別（当期/前期/前々期等）"
        varchar     period_unit                 "期間単位"
        varchar     consolidated_type           "連結区分（連結/個別）"
        varchar     context_ref                 "XBRLコンテキスト参照"
        decimal     amount                      "金額（精度: 20桁4小数）"
        int         display_scale               "表示単位（千円/百万円等）"
        varchar     currency                    "通貨"
    }

    company               ||--o{ financial_document : "1社 複数書類"
    financial_document    ||--o{ financial_data      : "1書類 複数勘定科目"
    account_master        ||--o{ financial_data      : "1科目 複数データ"
```

### Phase 2

```mermaid
erDiagram

    financial_document {
        bigint  id  PK
    }

    company {
        bigint  id  PK
    }

    financial_metrics {
        bigint      id                      PK  "GENERATED ALWAYS AS IDENTITY"
        bigint      financial_document_id   FK
        decimal     roe                         "ROE（%）"
        decimal     roa                         "ROA（%）"
        decimal     operating_margin            "営業利益率（%）"
        decimal     equity_ratio                "自己資本比率（%）"
        decimal     cash_ratio                  "現金比率（%）"
        decimal     per                         "PER（株価API連携後に追加・nullable）"
        decimal     pbr                         "PBR（株価API連携後に追加・nullable）"
        timestamp   calculated_at
        timestamp   created_at
    }

    users {
        bigint      id              PK  "GENERATED ALWAYS AS IDENTITY"
        varchar     email           UK
        varchar     password_hash
        varchar     display_name
        varchar     plan                "free / premium"
        timestamp   created_at
        timestamp   updated_at
    }

    subscriptions {
        bigint      id                      PK  "GENERATED ALWAYS AS IDENTITY"
        bigint      user_id                 FK
        varchar     stripe_subscription_id      "Stripe サブスクリプションID"
        varchar     status                      "active / canceled / past_due"
        date        current_period_start
        date        current_period_end
        timestamp   created_at
        timestamp   updated_at
    }

    watchlists {
        bigint      id          PK  "GENERATED ALWAYS AS IDENTITY"
        bigint      user_id     FK
        varchar     name            "ウォッチリスト名"
        timestamp   created_at
        timestamp   updated_at
    }

    watchlist_items {
        bigint      id              PK  "GENERATED ALWAYS AS IDENTITY"
        bigint      watchlist_id    FK
        bigint      company_id      FK
        timestamp   added_at
    }

    financial_document  ||--o|  financial_metrics  : "1書類 1指標セット"
    users               ||--o|  subscriptions      : "1ユーザー 1サブスク"
    users               ||--o{ watchlists          : "1ユーザー 複数リスト"
    watchlists          ||--o{ watchlist_items      : "1リスト 複数銘柄"
    company             ||--o{ watchlist_items      : "1社 複数リストに登録"
```

---

## 補足説明

### document_metadata と financial_document の関係
- `document_metadata`：EDINET書類一覧API（日付指定）で取得したレコードを保存
- `financial_document`：EDINET書類取得APIで取得した有価証券報告書の情報を保存
- 両テーブルは `document_id` で対応するが、**FK制約なし**
  - 理由：書類一覧APIはEDINETコード指定不可（日付のみ）のため、手動でEDINETコードを指定して書類取得APIを直接叩くケースがある。その場合 `document_metadata` にレコードが存在しない

### financial_data の consolidated_type
- 有価証券報告書には連結・個別両方の数値が含まれるため、どちらのデータかを区別する
- 財務指標の計算時は連結を優先する

### financial_metrics（Phase 2）
- Phase 1では `financial_data` から都度計算してレスポンスする
- Phase 2のスクリーニング導入時に追加。指標をキャッシュしてWHERE条件での高速絞り込みを可能にする
- 指標追加時のオペレーション：カラム追加（Flyway） → 全レコードバッチ再計算

### インデックス方針
**Phase 1**
- `document_metadata.document_id`（UNIQUE）
- `company.edinet_code`（UNIQUE）
- `company.security_code`（UNIQUE）
- `financial_document.document_id`（UNIQUE）
- `financial_document.company_id`
- `financial_data.financial_document_id`
- `financial_data.account_id`

**Phase 2（スクリーニング用）**
- `financial_metrics.cash_ratio`
- `financial_metrics.roe`、`roa`、`equity_ratio`
