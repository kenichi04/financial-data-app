# ADR-0001: 財務指標計算における勘定科目マッピングの前提

最終更新: 2026-07-05

## ステータス

Accepted（MVP時点。将来見直しの余地あり）

## 背景

`docs/requirements.md` §3.2 のROE・ROA・営業利益率・自己資本比率・現金比率（M-01〜M-04, N-01）を、EDINETの標準勘定科目コード（`account_master`）から算出するにあたり、以下の非自明な前提を置いた。実装は `FinancialAccountResolver` / `FinancialMetricsCalculator`（`backend/src/main/java/com/example/financialdataapp/`）。

## 決定事項

- **自己資本の代用**: ROEの分母・自己資本比率の分子となる「自己資本」は、本来は `ShareholdersEquity`（株主資本）だが、MVPでは `NetAssets`（純資産）で代用する。非支配株主持分を含むため厳密には異なるが、多くの企業では差が小さく、MVP段階では簡略化する。
- **現金同等物の代用**: 現金比率の「現金及び現金同等物」は、CF計算書上の期末残高科目が `account_master` に未整備のため、BS上の `CashAndDeposits`（現金及び預金）で代用する。
- **売上高の勘定科目フォールバック順序**: 会計基準・業種によって科目名が異なるため、`NetSales → Revenue → OperatingRevenue1 → OperatingRevenue2` の順に存在チェックし、最初に見つかったものを採用する。
- **連結・単体の優先順位**: 連結データ（`ConsolidatedType.CONSOLIDATED`）があれば優先し、無い場合のみ単体（`NON_CONSOLIDATED`）を使う。
- **対象年度**: 当期（`YearType.CURRENT_YEAR`）のみを対象とし、前期データは使用しない。

## 影響

- 個別の勘定科目が存在しない場合、対応する指標フィールドは例外を投げず `null` を返す。
- 将来、自己資本をより厳密に扱いたい場合は `ShareholdersEquity` への切り替えを検討する（`account_master` には既に登録済み）。
