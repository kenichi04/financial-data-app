# jOOQコード生成の現状の問題点と段階的改善プラン

最終更新: 2026-07-05（Step 1 実施済み: `feature/jooq-codegen-step1` ブランチ）
Claude Code 作成

> **実施状況**: Step 1 は実施済み。「現状の構成」「問題点」の節は実施前（`~/.m2/settings.xml` + 全ビルドで生成）の記録として残している。Step 2 以降は未実施。

---

## 前提: jOOQ自体の採用は妥当

「書き込みはJPA・読み取りはjOOQ」というCQRS構成は本プロジェクトに適している。今後の指標ランキング・スクリーニングのような込み入ったSELECTを型安全に書ける。**本ドキュメントで扱うのはjOOQの是非ではなく、コード生成（codegen）の実行方法のみ**。

## 現状の構成（backend/pom.xml）

- `jooq-codegen-maven` プラグインが **`generate-sources` フェーズに無条件でバインド**されており、`./mvnw generate-sources` だけでなく `test` / `package` を含む**すべてのMavenビルドで生成が走る**
- 生成の元ネタは**起動中のPostgreSQLへのJDBC接続**（`${db.url}` / `${db.user}` / `${db.password}`）
- 接続情報はリポジトリ外の **`~/.m2/settings.xml`** に定義（flyway-maven-plugin も同じプロパティを参照）
- 生成物は `target/generated-sources/jooq/` に出力（Git管理外。ビルドのたびに再生成）
- `jooq-meta` / `jooq-codegen` がアプリの `<dependencies>` にも入っている（プラグインが自前で持つため、アプリの依存としては不要。pom内のコメントにも「不要かも」と記載あり）

## 問題点

### 1. ビルドが「マシンの隠れた状態」に依存する（最重要）

`git clone` → `./mvnw test` が通るためには「DBが起動済み」「Flywayマイグレーション適用済み」「`~/.m2/settings.xml` に接続プロパティあり」という、**リポジトリの外にある3条件**が必要。この結果:

- **GitHub Actions（CI）でビルドできない** — CIのマシンにはsettings.xmlもDBもない
- **Dockerfileの中で `mvn package` できない** — 同上。Render等へのコンテナデプロイの直接の障害になる
- 新しいマシン・新しい開発者のセットアップ手順がリポジトリだけで完結しない

今後予定しているDockerfile化・CI整備・クラウドデプロイは、この問題を解消しないと進められない。

### 2. テスト実行にすらDBが要る

テストは `@ExtendWith(MockitoExtension.class)` の純粋ユニットテストでDB不要のはずだが、Mavenライフサイクル上 `generate-sources` が `test` より先に走るため、**DBを止めていると `./mvnw test` が生成段階で失敗する**。テストの思想（Spring Context不使用・インフラ非依存）とビルドの実態がズレている。

### 3. 接続情報の置き場所が過剰防衛かつ属人的

`~/.m2/settings.xml` は本来、社内リポジトリ認証等の「ビルドインフラ設定」を置く場所。ローカルのdocker-compose用DB接続情報（localhost・開発用ユーザー）は秘密情報ではないので、リポジトリにコミットしてよい。外に出したことで秘匿性は上がらず、属人性だけが上がっている。

## 改善方式の比較

| 方式 | ビルド時のDB | CI/Docker | スキーマとの正確な一致 | 導入の手間 |
|---|---|---|---|---|
| A. 起動中のDBから生成（現状） | 必要 | ✗ 通らない | ◎ | — |
| B. 生成物をGitにコミット | 不要 | ◎ | ○（再生成の規律次第） | 小 |
| C. FlywayのSQLから直接生成（DDLDatabase） | 不要 | ◎ | ○（SQL方言の解釈差が稀にある） | 中 |
| D. ビルド中に使い捨てDBを起動して生成（Testcontainers） | 不要（Dockerは必要） | ◎ | ◎ | 中 |

## 段階的改善プラン（推奨）

### Step 1: 生成をオプトイン化し、生成物をコミットする【すぐ】

方式Bへの移行。変更は `backend/pom.xml` 中心で小さい:

1. **jOOQ生成をMavenプロファイル（例: `-Pjooq-codegen`）に移し、通常ビルドでは走らないようにする**
   → `./mvnw test` / `package` がDBなしで通るようになる（問題1・2の解消）
2. **生成先を `target/` からコミット対象のディレクトリ（例: `backend/src/generated/jooq/`）に変更**し、build-helperのソース追加パスも合わせる。生成物をGit管理に含める
3. **接続情報のデフォルト値（docker-composeの開発用DB）を `pom.xml` のプロパティとしてコミット**する。秘密情報ではないため問題なく、`~/.m2/settings.xml` は不要になる（問題3の解消）
4. アプリの `<dependencies>` から `jooq-meta` / `jooq-codegen` を削除（プラグイン側で完結）
5. README / CLAUDE.md の手順を更新（「スキーマ変更時は `docker-compose up -d` → `./mvnw generate-sources -Pjooq-codegen` → 生成物をコミット」）

運用ルール: **Flywayマイグレーションを追加したPR/コミットには、再生成したjOOQコードを必ず含める**。

このルールは規律任せにせず、pre-commitフック（`.githooks/pre-commit`。有効化は `git config core.hooksPath .githooks`）で機械的にチェックする。フックは「マイグレーション変更あり・生成物変更なし」のコミットを拒否する。ただし:

- 生成物に差分が出ないマイグレーション（データ投入のみ・インデックス追加のみ等）は `git commit --no-verify` で通してよい（誤検知は許容）
- フックが検出できるのは「入れ忘れ」（clone先・CIでのコンパイルエラーの原因）のみ。**カラム型変更等で生成物が古いままでもコンパイルが通ってしまうケース**（実行時に初めてズレが発覚する）は検出できない。これはStep 3のCI再生成一致チェックで担保する

### Step 2: ビルド内生成に移行する【CI整備時に判断】

方式Dへの移行。`testcontainers-jooq-codegen-maven-plugin` を使うと、ビルド中に使い捨てPostgreSQLコンテナ起動 → Flywayマイグレーション適用 → jOOQ生成、までが自動で完結する。

- 利点: 実物のPostgreSQLから生成するので正確さは現状方式と同等のまま、ビルドがどこでも自己完結する。生成物のコミットも「再生成忘れ」の規律も不要になる
- 前提: ビルド環境にDockerが必要（GitHub Actionsは標準で利用可。Dockerfile内ビルドの場合はStep 1の生成物コミット方式を併用するのが現実的）
- Dockerが使えない環境を重視するなら方式C（`jooq-meta-extensions` の DDLDatabase。FlywayのSQLファイルだけから生成）が代替。本プロジェクトのマイグレーションは標準的なSQLなので動く見込みだが、PostgreSQL固有構文が増えると解釈差のリスクがある

**判断基準**: Step 1で困っていなければ無理に移行しなくてよい。「再生成を忘れてCIが古い生成物のまま通ってしまった」等の事故が起きたら移行のサイン。

### Step 3: 整合性チェックの自動化【任意・CI成熟後】

Step 1運用を続ける場合の保険として、CIに「jOOQ再生成 → `git diff --exit-code`」のジョブを足すと、マイグレーションと生成物の不整合を機械的に検出できる（Testcontainers前提）。Step 2に移行した場合は不要。

## 影響範囲まとめ

- 変更するファイル: `backend/pom.xml`（プロファイル化・生成先変更・デフォルトプロパティ・依存削除）、`.gitignore`（生成ディレクトリの扱い）、`README.md` / `CLAUDE.md`（手順更新）
- アプリコード（`Jooq*QueryService` 等）への影響: **なし**（生成されるクラスは同一。参照パッケージも変えない）
- `~/.m2/settings.xml`: Step 1完了後は不要になる（削除してよい）
