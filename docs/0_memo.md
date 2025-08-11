## 開発用メモ
- EDINET 書類取得API用のテスト用docId: S100TGZR、S100VAU4
- DB: PostgreSQL
  - Dockerで起動 > docker compose up -d
- JOOQ用のDB接続設定
  - 接続情報はプロジェクト外の ~/.m2/settings.xml に定義
- JOOQ Codegen
  - 実行コマンド > ./mvnw jooq-codegen:generate@generate-jooq
  - 次のコマンドでもよい > ./mvnw generate-sources もしくは ./mvnw compile

  - コンテキストIDの定義で連結・単独、当期・前期が確認できる
  - インスタンスガイドラインp39のコンテキストの設定例
  - 一つのインラインXBRLファイルには一つの墨付き【】必要らしい
  - 表紙ファイルでコンテキストの定義見れる
  - 本文ファイルで参照しているコンテキストIDを表紙ファイルで定義している

## メモ
- Accountに登録する科目洗い出し
  - EDINETタクソノミの科目一覧ツリー確認
  - EDINETタクソノミ（語彙層）の語彙スキーマが基本か
  - 有価証券報告書ではEDINETタクソノミのうち以下が参照される？
    - 内閣府令タクソノミ、財務諸表本表タクソノミ、DEIタクソノミ
  - 提出者別タクソノミのスキーマファイル(.xsd)で参照先など確認できる
  　- スキーマファイル名で様式確認できる
  　- 各リンクファイル(xml)で名称や計算リンク
- 書類情報の確認方法
  - 様式ツリーで確認できそう
  - タクソノミ分割単位 > jpcrpで良さそう？
    - 財務諸表本表でjppfsもあるみたい
- EDINETタクソノミのフォルダ確認
  - jpcrp - {日付} - jpcrp_cor(rt)_{日付}.xsd > 語彙スキーマ, ロールタイプスキーマ


## TODO
- [ ] financial data 取得するファイルの抽出処理の修正
  - [ ] ファイル名の取得処理を修正
  - [ ] BSとPLをタグで区別する処理を追加
- [ ] Edinetのバリデーション変更
  - E + 数字5桁


## ターゲットファイル選択案
- 隅付き括弧【】でファイル名紐づいているはず（詳細タグ付け）
  - ターゲットとなるファイル名プレフィックスを持たせて、合致するファイルを抽出
  - docの1_EDINETタクソノミの概要説明.mdの末尾に記載
- 目次項目を示す要素あり。これ使えないか
  - 代替グループ（substitutionGroup）
  - 目次項目 > iod:identifierItem
  - docの2_提出者別タクソノミ作成ガイドライン.mdの5章に記載
- 拡張リンクロール使えないか
  - 「添付3 拡張リンクロール一覧」> EDINETタクソノミで使用している拡張リンクロール一覧
  - docの2_提出者別タクソノミ作成ガイドライン.mdの5章に記載
- BS, PLのタグを確認する
  - EdinetFileExtractionServiceのTODO記載
  - 例：jpcrp_cor:BalanceSheetTextBlock
    - 連結と単独でタグ違うように見える