# API仕様書【仮】

> **このドキュメントは仮のものです。内容は検討中であり、変更される可能性があります。**

最終更新: 2026-03-22

---

## 目次

1. [共通仕様](#共通仕様)
2. [エンドポイント一覧](#エンドポイント一覧)
3. [Phase 1 API 詳細](#phase-1-api-詳細)
4. [Phase 2 API 詳細](#phase-2-api-詳細)

---

## 共通仕様

### ベースURL

| 環境 | URL |
|---|---|
| 開発 | `http://localhost:8080` |
| 本番（MVP） | `https://<app>.onrender.com` |

### コンテントタイプ

- リクエスト: `Content-Type: application/json`
- レスポンス: `Content-Type: application/json`

### 認証方式

- Phase 1：認証なし
- Phase 2〜：JWT Bearer トークン

```
Authorization: Bearer <token>
```

ログインエンドポイント（A-202）で取得したトークンを全リクエストのヘッダーに付与する。

### 共通エラーレスポンス

全エンドポイントで以下の形式でエラーを返す。

```json
{
  "error": {
    "code": "COMPANY_NOT_FOUND",
    "message": "指定された企業が見つかりません"
  }
}
```

| エラーコード | HTTP ステータス | 説明 |
|---|---|---|
| `INVALID_PARAMETER` | 400 | リクエストパラメータが不正 |
| `UNAUTHORIZED` | 401 | 認証が必要（トークン未付与・期限切れ） |
| `FORBIDDEN` | 403 | 権限なし（プラン不足など） |
| `COMPANY_NOT_FOUND` | 404 | 企業が見つからない |
| `WATCHLIST_NOT_FOUND` | 404 | ウォッチリストが見つからない |
| `DUPLICATE_EMAIL` | 409 | メールアドレスが既に登録済み |
| `INTERNAL_SERVER_ERROR` | 500 | サーバー内部エラー |

### ページネーション

検索・一覧系の GET エンドポイントはページネーションをサポートする。

**クエリパラメータ**

| パラメータ | 型 | デフォルト | 説明 |
|---|---|---|---|
| `page` | integer | `0` | ページ番号（0始まり） |
| `size` | integer | `20` | 1ページあたりの件数（最大100） |

**レスポンス構造**

```json
{
  "content": [...],
  "totalElements": 150,
  "totalPages": 8,
  "page": 0,
  "size": 20
}
```

---

## エンドポイント一覧

### Phase 1

| ID | メソッド | パス | 概要 | 認証 |
|---|---|---|---|---|
| A-101 | GET | `/api/companies` | 銘柄検索 | 不要 |
| A-102 | GET | `/api/companies/{securityCode}` | 企業詳細取得 | 不要 |
| A-103 | GET | `/api/companies/{securityCode}/metrics` | 直近期の財務指標取得 | 不要 |
| A-104 | GET | `/api/companies/{securityCode}/metrics/history` | 財務指標時系列取得 | 不要 |

### Phase 2

| ID | メソッド | パス | 概要 | 認証 | 権限 |
|---|---|---|---|---|---|
| A-201 | POST | `/api/auth/register` | ユーザー登録 | 不要 | — |
| A-202 | POST | `/api/auth/login` | ログイン（JWT発行） | 不要 | — |
| A-203 | POST | `/api/auth/logout` | ログアウト | 必要 | — |
| A-204 | GET | `/api/users/me` | 認証ユーザー情報取得 | 必要 | — |
| A-205 | GET | `/api/screening` | スクリーニング | 必要 | premium |
| A-206 | GET | `/api/watchlists` | ウォッチリスト一覧取得 | 必要 | premium |
| A-207 | POST | `/api/watchlists` | ウォッチリスト作成 | 必要 | premium |
| A-208 | DELETE | `/api/watchlists/{id}` | ウォッチリスト削除 | 必要 | premium（本人のみ） |
| A-209 | GET | `/api/watchlists/{id}/items` | リスト内銘柄一覧取得 | 必要 | premium（本人のみ） |
| A-210 | POST | `/api/watchlists/{id}/items` | 銘柄追加 | 必要 | premium（本人のみ） |
| A-211 | DELETE | `/api/watchlists/{id}/items/{companyId}` | 銘柄削除 | 必要 | premium（本人のみ） |
| A-212 | GET | `/api/subscriptions/me` | サブスク情報取得 | 必要 | — |
| A-213 | POST | `/api/subscriptions/checkout` | Stripe Checkout セッション作成 | 必要 | — |
| A-214 | POST | `/api/subscriptions/cancel` | サブスク解約 | 必要 | — |
| A-215 | POST | `/api/webhooks/stripe` | Stripe Webhook 受信 | 不要（署名検証） | — |

---

## Phase 1 API 詳細

---

### A-101 銘柄検索

```
GET /api/companies
```

#### 概要

企業名（部分一致）または証券コード（前方一致）でキーワード検索する。結果に直近期の主要財務指標を含める。

#### クエリパラメータ

| パラメータ | 型 | 必須 | 説明 |
|---|---|---|---|
| `q` | string | 必須 | 検索キーワード（1文字以上）。企業名（部分一致）または証券コード（前方一致） |
| `page` | integer | 任意 | ページ番号（デフォルト: 0） |
| `size` | integer | 任意 | 件数（デフォルト: 20） |

#### 成功レスポンス `200 OK`

```json
{
  "content": [
    {
      "securityCode": "7203",
      "edinetCode": "E02144",
      "name": "トヨタ自動車株式会社",
      "latestFiscalYearEndDate": "2024-03-31",
      "latestMetrics": {
        "roe": 18.5,
        "roa": 5.2,
        "cashRatio": 12.3
      }
    }
  ],
  "totalElements": 3,
  "totalPages": 1,
  "page": 0,
  "size": 20
}
```

#### エラーレスポンス

| ステータス | コード | 条件 |
|---|---|---|
| 400 | `INVALID_PARAMETER` | `q` が未指定または空文字 |

---

### A-102 企業詳細取得

```
GET /api/companies/{securityCode}
```

#### 概要

証券コードで特定の企業の基本情報を取得する。

#### パスパラメータ

| パラメータ | 型 | 説明 |
|---|---|---|
| `securityCode` | string | 証券コード（4桁） |

#### 成功レスポンス `200 OK`

```json
{
  "securityCode": "7203",
  "edinetCode": "E02144",
  "name": "トヨタ自動車株式会社",
  "corporateNumber": "4120001004162",
  "latestFiscalYearEndDate": "2024-03-31"
}
```

#### エラーレスポンス

| ステータス | コード | 条件 |
|---|---|---|
| 404 | `COMPANY_NOT_FOUND` | 指定した証券コードの企業が存在しない |

---

### A-103 直近期の財務指標取得

```
GET /api/companies/{securityCode}/metrics
```

#### 概要

指定した企業の直近1期分の財務指標を返す。企業詳細画面の指標カード表示に使用する。連結財務諸表を優先し、連結がない場合は個別を使用する。

#### パスパラメータ

| パラメータ | 型 | 説明 |
|---|---|---|
| `securityCode` | string | 証券コード（4桁） |

#### 成功レスポンス `200 OK`

```json
{
  "securityCode": "7203",
  "fiscalYearEndDate": "2024-03-31",
  "consolidatedType": "連結",
  "metrics": {
    "roe": 18.5,
    "roa": 5.2,
    "operatingMargin": 9.8,
    "equityRatio": 35.4,
    "cashRatio": 12.3
  }
}
```

- 指標が計算不能（ゼロ除算など）の場合、該当フィールドは `null` を返す
- Phase 2 で株価API連携後に `per`・`pbr` フィールドを追加

#### エラーレスポンス

| ステータス | コード | 条件 |
|---|---|---|
| 404 | `COMPANY_NOT_FOUND` | 指定した証券コードの企業が存在しない |

---

### A-104 財務指標時系列取得

```
GET /api/companies/{securityCode}/metrics/history
```

#### 概要

指定した企業の財務指標を複数期分返す。企業詳細画面の時系列グラフに使用する。連結優先・連結なければ個別。

#### パスパラメータ

| パラメータ | 型 | 説明 |
|---|---|---|
| `securityCode` | string | 証券コード（4桁） |

#### クエリパラメータ

| パラメータ | 型 | デフォルト | 説明 |
|---|---|---|---|
| `years` | integer | `5` | 取得する期数（1〜10） |

#### 成功レスポンス `200 OK`

```json
{
  "securityCode": "7203",
  "consolidatedType": "連結",
  "history": [
    {
      "fiscalYearEndDate": "2020-03-31",
      "metrics": {
        "roe": 3.1,
        "roa": 0.9,
        "operatingMargin": 2.1,
        "equityRatio": 33.2,
        "cashRatio": 11.0
      }
    },
    {
      "fiscalYearEndDate": "2021-03-31",
      "metrics": {
        "roe": 6.2,
        "roa": 1.8,
        "operatingMargin": 3.5,
        "equityRatio": 34.1,
        "cashRatio": 11.5
      }
    }
  ]
}
```

- `history` は決算期末日の昇順で返す
- データが存在しない期は含まない

#### エラーレスポンス

| ステータス | コード | 条件 |
|---|---|---|
| 400 | `INVALID_PARAMETER` | `years` が範囲外（1未満または10超） |
| 404 | `COMPANY_NOT_FOUND` | 指定した証券コードの企業が存在しない |

---

## Phase 2 API 詳細

---

### A-201 ユーザー登録

```
POST /api/auth/register
```

#### リクエストボディ

```json
{
  "email": "user@example.com",
  "password": "password123",
  "displayName": "山田太郎"
}
```

| フィールド | 型 | 必須 | バリデーション |
|---|---|---|---|
| `email` | string | 必須 | メール形式 |
| `password` | string | 必須 | 8文字以上 |
| `displayName` | string | 必須 | 1〜50文字 |

#### 成功レスポンス `201 Created`

```json
{
  "userId": 1,
  "email": "user@example.com",
  "displayName": "山田太郎",
  "plan": "free"
}
```

#### エラーレスポンス

| ステータス | コード | 条件 |
|---|---|---|
| 400 | `INVALID_PARAMETER` | バリデーション違反 |
| 409 | `DUPLICATE_EMAIL` | メールアドレスが既に登録済み |

---

### A-202 ログイン

```
POST /api/auth/login
```

#### リクエストボディ

```json
{
  "email": "user@example.com",
  "password": "password123"
}
```

#### 成功レスポンス `200 OK`

```json
{
  "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "tokenType": "Bearer",
  "expiresIn": 86400
}
```

- `expiresIn`：トークン有効期間（秒）。デフォルト86400秒（24時間）

#### エラーレスポンス

| ステータス | コード | 条件 |
|---|---|---|
| 401 | `UNAUTHORIZED` | メールアドレスまたはパスワードが不正 |

---

### A-203 ログアウト

```
POST /api/auth/logout
```

#### 認証

必要（`Authorization: Bearer <token>`）

#### 成功レスポンス `204 No Content`

#### 備考

- サーバーサイドでトークンを無効化する実装の場合はブロックリストに追加
- クライアント側でトークンを破棄すれば十分な場合は204のみ返す

---

### A-204 認証ユーザー情報取得

```
GET /api/users/me
```

#### 認証

必要

#### 成功レスポンス `200 OK`

```json
{
  "userId": 1,
  "email": "user@example.com",
  "displayName": "山田太郎",
  "plan": "premium",
  "subscriptionStatus": "active",
  "subscriptionPeriodEnd": "2026-04-22"
}
```

- `plan`: `"free"` or `"premium"`
- `subscriptionStatus`: `"active"` / `"canceled"` / `"past_due"` / `null`（free の場合）

---

### A-205 スクリーニング

```
GET /api/screening
```

#### 認証

必要（premium プランのみ）

#### クエリパラメータ

全パラメータ任意。未指定の場合はその指標での絞り込みを行わない。

| パラメータ | 型 | 説明 |
|---|---|---|
| `roeMin` | number | ROE 最小値（%） |
| `roeMax` | number | ROE 最大値（%） |
| `roaMin` | number | ROA 最小値（%） |
| `roaMax` | number | ROA 最大値（%） |
| `operatingMarginMin` | number | 営業利益率 最小値（%） |
| `operatingMarginMax` | number | 営業利益率 最大値（%） |
| `equityRatioMin` | number | 自己資本比率 最小値（%） |
| `equityRatioMax` | number | 自己資本比率 最大値（%） |
| `cashRatioMin` | number | 現金比率 最小値（%） |
| `cashRatioMax` | number | 現金比率 最大値（%） |
| `page` | integer | ページ番号（デフォルト: 0） |
| `size` | integer | 件数（デフォルト: 20） |

#### 成功レスポンス `200 OK`

```json
{
  "content": [
    {
      "securityCode": "7203",
      "name": "トヨタ自動車株式会社",
      "fiscalYearEndDate": "2024-03-31",
      "metrics": {
        "roe": 18.5,
        "roa": 5.2,
        "operatingMargin": 9.8,
        "equityRatio": 35.4,
        "cashRatio": 12.3
      }
    }
  ],
  "totalElements": 45,
  "totalPages": 3,
  "page": 0,
  "size": 20
}
```

#### エラーレスポンス

| ステータス | コード | 条件 |
|---|---|---|
| 401 | `UNAUTHORIZED` | 未認証 |
| 403 | `FORBIDDEN` | free プランユーザー |

---

### A-206 ウォッチリスト一覧取得

```
GET /api/watchlists
```

#### 認証

必要（premium プランのみ）

#### 成功レスポンス `200 OK`

```json
[
  {
    "id": 1,
    "name": "気になる銘柄",
    "itemCount": 5,
    "createdAt": "2026-01-15T10:00:00Z"
  },
  {
    "id": 2,
    "name": "割安株候補",
    "itemCount": 3,
    "createdAt": "2026-02-01T09:30:00Z"
  }
]
```

---

### A-207 ウォッチリスト作成

```
POST /api/watchlists
```

#### 認証

必要（premium プランのみ）

#### リクエストボディ

```json
{
  "name": "新規リスト"
}
```

| フィールド | 型 | 必須 | バリデーション |
|---|---|---|---|
| `name` | string | 必須 | 1〜50文字 |

#### 成功レスポンス `201 Created`

```json
{
  "id": 3,
  "name": "新規リスト",
  "itemCount": 0,
  "createdAt": "2026-03-22T12:00:00Z"
}
```

---

### A-208 ウォッチリスト削除

```
DELETE /api/watchlists/{id}
```

#### 認証

必要（本人のリストのみ）

#### パスパラメータ

| パラメータ | 型 | 説明 |
|---|---|---|
| `id` | integer | ウォッチリストID |

#### 成功レスポンス `204 No Content`

#### エラーレスポンス

| ステータス | コード | 条件 |
|---|---|---|
| 403 | `FORBIDDEN` | 他ユーザーのリスト |
| 404 | `WATCHLIST_NOT_FOUND` | リストが存在しない |

---

### A-209 リスト内銘柄一覧取得

```
GET /api/watchlists/{id}/items
```

#### 認証

必要（本人のリストのみ）

#### パスパラメータ

| パラメータ | 型 | 説明 |
|---|---|---|
| `id` | integer | ウォッチリストID |

#### 成功レスポンス `200 OK`

```json
[
  {
    "companyId": 1,
    "securityCode": "7203",
    "name": "トヨタ自動車株式会社",
    "addedAt": "2026-03-01T10:00:00Z",
    "latestMetrics": {
      "roe": 18.5,
      "roa": 5.2,
      "cashRatio": 12.3
    }
  }
]
```

#### エラーレスポンス

| ステータス | コード | 条件 |
|---|---|---|
| 403 | `FORBIDDEN` | 他ユーザーのリスト |
| 404 | `WATCHLIST_NOT_FOUND` | リストが存在しない |

---

### A-210 銘柄追加

```
POST /api/watchlists/{id}/items
```

#### 認証

必要（本人のリストのみ）

#### パスパラメータ

| パラメータ | 型 | 説明 |
|---|---|---|
| `id` | integer | ウォッチリストID |

#### リクエストボディ

```json
{
  "securityCode": "7203"
}
```

#### 成功レスポンス `201 Created`

```json
{
  "companyId": 1,
  "securityCode": "7203",
  "name": "トヨタ自動車株式会社",
  "addedAt": "2026-03-22T12:00:00Z"
}
```

#### エラーレスポンス

| ステータス | コード | 条件 |
|---|---|---|
| 400 | `INVALID_PARAMETER` | 既にリストに登録済み |
| 403 | `FORBIDDEN` | 他ユーザーのリスト |
| 404 | `WATCHLIST_NOT_FOUND` | リストが存在しない |
| 404 | `COMPANY_NOT_FOUND` | 指定した証券コードの企業が存在しない |

---

### A-211 銘柄削除

```
DELETE /api/watchlists/{id}/items/{companyId}
```

#### 認証

必要（本人のリストのみ）

#### パスパラメータ

| パラメータ | 型 | 説明 |
|---|---|---|
| `id` | integer | ウォッチリストID |
| `companyId` | integer | 企業ID（`company.id`） |

#### 成功レスポンス `204 No Content`

#### エラーレスポンス

| ステータス | コード | 条件 |
|---|---|---|
| 403 | `FORBIDDEN` | 他ユーザーのリスト |
| 404 | `WATCHLIST_NOT_FOUND` | リストまたは銘柄が存在しない |

---

### A-212 サブスク情報取得

```
GET /api/subscriptions/me
```

#### 認証

必要

#### 成功レスポンス `200 OK`

```json
{
  "plan": "premium",
  "status": "active",
  "currentPeriodStart": "2026-03-22",
  "currentPeriodEnd": "2026-04-22",
  "stripeSubscriptionId": "sub_xxxxxxxxxxxxx"
}
```

- `plan`: `"free"` or `"premium"`
- `status`: `"active"` / `"canceled"` / `"past_due"` / `null`（free の場合）

---

### A-213 Stripe Checkout セッション作成

```
POST /api/subscriptions/checkout
```

#### 認証

必要

#### リクエストボディ

なし

#### 成功レスポンス `200 OK`

```json
{
  "checkoutUrl": "https://checkout.stripe.com/c/pay/..."
}
```

- フロントエンドはこの URL に遷移して Stripe の決済画面を表示する
- 決済完了後、Stripe は設定した `successUrl` にリダイレクト

#### エラーレスポンス

| ステータス | コード | 条件 |
|---|---|---|
| 400 | `INVALID_PARAMETER` | 既に premium プランに加入済み |

---

### A-214 サブスク解約

```
POST /api/subscriptions/cancel
```

#### 認証

必要

#### リクエストボディ

なし

#### 成功レスポンス `200 OK`

```json
{
  "status": "canceled",
  "currentPeriodEnd": "2026-04-22"
}
```

- 即時解約ではなく、現在の契約期間終了時に解約される（`cancel_at_period_end: true`）
- `currentPeriodEnd` まではプレミアム機能を利用可能

#### エラーレスポンス

| ステータス | コード | 条件 |
|---|---|---|
| 400 | `INVALID_PARAMETER` | free プランのため解約不要 |

---

### A-215 Stripe Webhook 受信

```
POST /api/webhooks/stripe
```

#### 認証

不要（Stripe 署名検証）

- `stripe-signature` ヘッダーの HMAC 署名を Stripe SDK で検証する
- 検証失敗時は `400 Bad Request` を返す

#### 処理するイベント

| イベント | 処理内容 |
|---|---|
| `checkout.session.completed` | `subscriptions` テーブルにレコード追加、`users.plan` を `premium` に更新 |
| `customer.subscription.updated` | `subscriptions` の `status` / 期間を更新 |
| `customer.subscription.deleted` | `users.plan` を `free` に更新 |
| `invoice.payment_failed` | `subscriptions.status` を `past_due` に更新 |

#### 成功レスポンス `200 OK`

- Stripe はレスポンスを受け取れなかった場合に再送するため、正常処理後は必ず 200 を返す

---

## 備考

- Phase 1 は認証なし。A-101〜A-104 のみ実装する。
- Phase 2 で認証・スクリーニング・ウォッチリスト・サブスクを追加する。
- 財務指標の計算は Phase 1 では `financial_data` テーブルから都度計算する。Phase 2 では `financial_metrics` テーブルにキャッシュしてスクリーニングを高速化する。
- PER・PBR は株価データ API 連携（Phase 2）後に A-103 / A-104 のレスポンスに追加する。
