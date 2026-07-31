# TODO / 技術的負債メモ

財務指標バッチ（`FinancialMetricsCalculationUseCase`）の N+1 解消リファクタリング時に確認した、
本タスクの対象外とした論点を記録しておく。

## 財務指標まわり

- **再計算手段がない**: `FinancialMetricsService.calculateAndSaveMetrics` は同一書類の既存行を
  置き換える upsert 対応だが、`IFinancialMetricsQueryService.findDocumentIdsWithoutMetrics()` が
  指標行のない書類しか返さないため、この upsert 能力に API から到達できない。計算式の修正時に
  既存の指標を再計算できない（force 再計算手段が必要）。
- **バッチが 1 リクエスト同期・全件処理**: `POST /api/financial-metrics/calculate` は未計算書類を
  同期で全件処理するため、件数増加で HTTP タイムアウトの恐れ。定期バッチ／非同期化と合わせて設計する。
- **失敗原因が追えない**: `catch (RuntimeException e)` で例外を握りつぶしており、`failedDocumentIds` に
  書類 ID が残るだけで理由が分からない。コードベース全体にロギングが無いため、ロガー導入の判断とセット。
- **`consolidated_type` の判定**: `EdinetContext.extractConsolidatedType` は `_NonConsolidatedMember` を
  含まない contextRef を全て `CONSOLIDATED` とみなす。連結子会社を持たない単体決算の会社も
  `CONSOLIDATED` として記録される可能性がある。要仕様確認。

## パフォーマンス

- **`AccountService.getAccounts()` のキャッシュ化**: 勘定科目マスタは実質不変なのに毎回全件 SELECT
  している（`code-review-2026-03-24.md` Issue #8）。`@Cacheable` 化を検討する。今回のバッチ内 1 回
  ロード（UseCase 側）とは独立した改善。

## テスト不足

- `FinancialMetricsController` のコントローラテストが無い。
- `JooqFinancialMetricsQueryService`（left-join-is-null クエリ）の結合テストが無い。
- **jOOQクエリ全般に結合テスト基盤が無い**: `JooqCompanyQueryService.searchCompanies` / `findBySecurityCode` と
  `LatestFinancialMetrics`（`DISTINCT ON` による直近期1行の選択）は、純ユニットテストでは検証できず
  Swagger UI の手動確認に頼っている。取込済みデータが1社しか無いため、ページネーションと
  「同一年度に連結・個別が混在する企業で連結が選ばれる」ケースは実データで未検証。
  Testcontainers 等の導入を検討する。
- `FinancialDataAppApplicationTests`（`@SpringBootTest`）は `DB_URL` / `DB_USER` / `DB_PASSWORD` /
  `EDINET_API_SUBSCRIPTION_KEY` / `DOWNLOAD_USER_DIR` が環境変数に無いと context load に失敗する
  （`.env` にはこれらが無く、POSTGRES_* のみ）。`src/test/resources/application-test.properties` を
  用意しないと素の `./mvnw test` がグリーンにならない。

## ロギング

- `GlobalExceptionHandler` で slf4j を導入した（500系とハンドルされない例外のみ記録）。
  コードベース全体のロギング方針（バッチの失敗理由など）は引き続き未整備。
