# コードレビュー結果 — 2026-03-24

**レビュー対象**: `backend/src/main/java/` および `backend/src/test/java/` 配下の全主要クラス
**検出問題数**: 12件（Critical: 2, High: 4, Medium: 4, Low: 2）
**全体評価**: DDD 4層構造の骨格は正しく設計されているが、Controllerがサービス層を直接参照している境界違反、ドメイン層からアプリ層への逆方向依存、そしてN+1クエリが目立つ修正ポイントである。

---

## Critical

### Issue #1 — ControllerがUseCaseを迂回してServiceを直接注入している

**Category**: Best Practice (DDD)
**Location**: `DocumentMetadataController.java` 全体、`FinancialDocumentController.java` 全体

**問題**:
`DocumentMetadataController` は `DocumentMetadataQueryUseCase` に加えて `DocumentMetadataService` を直接注入している。同様に `FinancialDocumentController` は `FinancialDocumentService` を直接注入している。これは DDD の layered architecture における最重要ルール違反で、Presentation 層が Application 層を飛び越えて依存関係を持つ構造になっている。`@Deprecated` なエンドポイント（`/v1/`）が今もアクティブなビーンを注入して動いている。

**影響**:
- 廃止済みのはずのサービス依存が残り続けることで、テストや変更時に誤って古いパスが呼ばれるリスク
- Controllerの依存関係が肥大化し、UseCaseレイヤーの意味がなくなる

**Current Code**:
```java
// DocumentMetadataController.java
public class DocumentMetadataController {
    private final DocumentMetadataQueryUseCase documentMetadataQueryUseCase;
    private final DocumentMetadataService documentMetadataService; // 直接注入

    @Deprecated
    @GetMapping("/v1/unprocessedMetadata")
    public List<DocumentMetadata> getUnprocessedDocumentMetadata() {
        return documentMetadataService.getUnprocessedMetadata(); // UseCaseを迂回
    }
}
```

**Improved Version**:
```java
@RestController
@RequestMapping("/api/document-metadata")
public class DocumentMetadataController {

    // UseCaseのみ注入 — Serviceへの直接参照は持たない
    private final DocumentMetadataQueryUseCase documentMetadataQueryUseCase;

    public DocumentMetadataController(DocumentMetadataQueryUseCase documentMetadataQueryUseCase) {
        this.documentMetadataQueryUseCase = documentMetadataQueryUseCase;
    }

    @GetMapping("/unprocessedMetadata")
    public List<DocumentMetadataDto> getUnprocessedMetadata() {
        return documentMetadataQueryUseCase.getUnProcessedMetadata();
    }

    @GetMapping("/companies")
    public List<CompanyDto> getAllCompanies() {
        return documentMetadataQueryUseCase.getCompanies();
    }
    // v1エンドポイントは削除
}
```

---

### Issue #2 — ドメイン層がアプリ層の定数クラスをimportしている（逆方向依存）

**Category**: Best Practice (DDD)
**Location**: `domain/model/context/EdinetContext.java` line 9

**問題**:
`EdinetContext` はドメインモデルであるにもかかわらず、`application/service/constants/XbrlConstants` をimportしている。DDD における依存方向は「外から内へ」（presentation → application → domain）であり、domain が application に依存するのはアーキテクチャ原則の根本的な違反である。

**影響**:
- `XbrlConstants` の構造を変えた場合、domain モデルのコンパイルが壊れる
- ドメインモデルを他プロジェクトに移植する際に application 層ごと持ち込む必要が生じる

**Current Code**:
```java
// EdinetContext.java (domain layer)
import static com.example.financialdataapp.application.service.constants.XbrlConstants.*;

public class EdinetContext {
    private static final Set<String> SUPPORTED_CONTEXT_REFS = Set.of(
            CONTEXT_CURRENT_YEAR_INSTANT,
            ...
    );
}
```

**Improved Version**:
```java
// domain/model/context/EdinetContext.java
// XbrlConstantsへの依存を排除し、定数をドメイン内で自己完結させる
public class EdinetContext {
    private static final Set<String> SUPPORTED_CONTEXT_REFS = Set.of(
            "CurrentYearInstant",
            "CurrentYearDuration",
            "CurrentYearInstant_NonConsolidatedMember",
            "CurrentYearDuration_NonConsolidatedMember",
            "Prior1YearInstant",
            "Prior1YearDuration",
            "Prior1YearInstant_NonConsolidatedMember",
            "Prior1YearDuration_NonConsolidatedMember"
    );
}
```

> 別案: `domain/constants/XbrlContextConstants.java` をドメイン層に新設し、contextRef 文字列定数のみをそちらに移動する。アプリ層の `XbrlConstants` はその他の namespace 定数（`ix:nonFraction` 等）を引き続き保持する。

---

## High

### Issue #3 — JooqFinancialDocumentQueryServiceでN+1クエリが発生している

**Category**: Performance
**Location**: `JooqFinancialDocumentQueryService.java` line 44–68

**問題**:
`fetchByFinancialDocumentId()` は最初のクエリで `financial_document` レコードを1件取得し、その結果の `.map()` ラムダ内で2つ目のクエリ（`financial_data` + `account_master` JOIN）を実行している。N+1パターンの典型で、将来的に複数ドキュメントを一括取得するAPIを追加した際に即座に問題になる。

**Current Code**:
```java
@Override
public Optional<FinancialDocumentDto> fetchByFinancialDocumentId(DocumentId documentId) {
    return create
            .select(...)
            .from(FINANCIAL_DOCUMENT)
            .where(FINANCIAL_DOCUMENT.DOCUMENT_ID.eq(documentId.toString()))
            .fetchOptional()
            .map(docRecord -> {
                // 2つ目のクエリをラムダ内で実行 — N+1
                var data = create
                        .select(...)
                        .from(FINANCIAL_DATA)
                        .join(ACCOUNT_MASTER)...
                        .where(FINANCIAL_DATA.FINANCIAL_DOCUMENT_ID.eq(...))
                        .fetch()
                        .map(this::toDataDto);
                return toDocumentDto(docRecord, data);
            });
}
```

**Improved Version**:
```java
@Override
public Optional<FinancialDocumentDto> fetchByFinancialDocumentId(DocumentId documentId) {
    // 1回のJOINクエリで financial_document + financial_data + account_master を取得
    var records = create
            .select(
                    FINANCIAL_DOCUMENT.ID,
                    FINANCIAL_DOCUMENT.DOCUMENT_ID,
                    FINANCIAL_DOCUMENT.EDINET_CODE,
                    FINANCIAL_DOCUMENT.DOCUMENT_TYPE,
                    FINANCIAL_DOCUMENT.FISCAL_YEAR_END_DATE,
                    FINANCIAL_DOCUMENT.CURRENCY,
                    FINANCIAL_DATA.ACCOUNT_ID,
                    ACCOUNT_MASTER.CODE,
                    ACCOUNT_MASTER.NAME_JP,
                    FINANCIAL_DATA.YEAR_TYPE,
                    FINANCIAL_DATA.PERIOD_UNIT,
                    FINANCIAL_DATA.CONSOLIDATED_TYPE,
                    FINANCIAL_DATA.AMOUNT,
                    FINANCIAL_DATA.DISPLAY_SCALE,
                    FINANCIAL_DATA.CURRENCY
            )
            .from(FINANCIAL_DOCUMENT)
            .join(FINANCIAL_DATA).on(FINANCIAL_DATA.FINANCIAL_DOCUMENT_ID.eq(FINANCIAL_DOCUMENT.ID))
            .join(ACCOUNT_MASTER).on(FINANCIAL_DATA.ACCOUNT_ID.eq(ACCOUNT_MASTER.ID))
            .where(FINANCIAL_DOCUMENT.DOCUMENT_ID.eq(documentId.toString()))
            .fetch();

    if (records.isEmpty()) {
        return Optional.empty();
    }

    var firstRecord = records.get(0);
    var dataList = records.stream().map(this::toDataDto).toList();

    return Optional.of(toDocumentDto(firstRecord, dataList));
}
```

---

### Issue #4 — DocumentMetadataServiceのfilterNewCompaniesが全件取得している

**Category**: Performance
**Location**: `DocumentMetadataService.java` lines 88–94

**問題**:
`filterNewCompanies()` は `companyRepository.findAll()` で全社情報を取得してからメモリ上でフィルタリングしている。会社数が数千件になった時点でパフォーマンスが劣化する。`filterNewMetadata()` が `findByDocumentIds()` でINクエリを使っているのと一貫性がない。

**Current Code**:
```java
private List<Company> filterNewCompanies(List<Company> companies) {
    var storedCompanyEdinetCodes = companyRepository.findAll().stream() // 全件取得
            .map(Company::getEdinetCode)
            .toList();
    return companies.stream()
            .filter(company -> !storedCompanyEdinetCodes.contains(company.getEdinetCode()))
            .toList();
}
```

**Improved Version**:
```java
// ICompanyRepository に新メソッドを追加
List<Company> findByEdinetCodes(List<EdinetCode> edinetCodes);

// JpaCompanyRepository に対応メソッドを追加
List<CompanyEntity> findByEdinetCodeIn(List<String> edinetCodes);

// DocumentMetadataService のフィルタリングを修正
private List<Company> filterNewCompanies(List<Company> companies) {
    var edinetCodes = companies.stream().map(Company::getEdinetCode).toList();
    var storedEdinetCodes = companyRepository.findByEdinetCodes(edinetCodes).stream()
            .map(Company::getEdinetCode)
            .toList();
    return companies.stream()
            .filter(company -> !storedEdinetCodes.contains(company.getEdinetCode()))
            .toList();
}
```

---

### Issue #5 — リポジトリの命名規則が設計方針と食い違っている

**Category**: Readability, Best Practice
**Location**: `infrastructure/database/jpa/repository/` パッケージ全体

**問題**:
CLAUDE.md の命名規則と現状の実装が逆になっている。

| 現状のクラス名 | 実態 | あるべき名前 |
|---|---|---|
| `DocumentMetadataRepository` | Spring Data JPA I/F | `JpaDocumentMetadataRepository` |
| `JpaDocumentMetadataRepository` | ドメインI/F実装 | `DocumentMetadataRepository` |
| `FinancialDocumentRepository` | Spring Data JPA I/F | `JpaFinancialDocumentRepository` |
| `JpaFinancialDocumentRepository` | ドメインI/F実装 | `FinancialDocumentRepository` |

**Improved Version**:
```java
// Spring Data JPA インタフェース（直接使用しない側）
public interface JpaDocumentMetadataRepository extends JpaRepository<DocumentMetadataEntity, Long> {
    DocumentMetadataEntity findByDocumentId(String documentId);
    List<DocumentMetadataEntity> findByDocumentIdIn(List<String> documentIds);
    List<DocumentMetadataEntity> findByProcessedFalse();
}

// ドメインインタフェースの JPA 実装（アプリ層から使われる側）
@Repository
@Primary
public class DocumentMetadataRepository implements IDocumentMetadataRepository {
    private final JpaDocumentMetadataRepository jpaDocumentMetadataRepository;
    // ...
}
```

---

### Issue #6 — グローバル例外ハンドラーが存在せずRuntimeExceptionが500で素通りする

**Category**: Best Practice
**Location**: プロジェクト全体（`@ControllerAdvice` クラスが存在しない）

**問題**:
多くのクラスが `RuntimeException` を直接スローしているが、捕捉するグローバルハンドラーがない。`IllegalArgumentException`（Document already exists）も 500 として返るが、クライアントからは 409 Conflict が期待される。

**Current Code**:
```java
// FinancialDocumentFetchUseCase.java
if (financialDocumentService.existsByDocumentId(documentId)) {
    throw new IllegalArgumentException("Document already exists: " + documentId);
    // -> 500 Internal Server Error として返ってしまう
}
```

**Improved Version**:
```java
// presentation/exception/GlobalExceptionHandler.java
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleBadRequest(IllegalArgumentException e) {
        return new ErrorResponse("BAD_REQUEST", e.getMessage());
    }

    @ExceptionHandler(IllegalStateException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleInternalError(IllegalStateException e) {
        return new ErrorResponse("INTERNAL_ERROR", e.getMessage());
    }
}

// presentation/exception/ErrorResponse.java
public record ErrorResponse(String code, String message) {}
```

> 注意: "Document already exists" は意味的には 409 Conflict が正しい。`DocumentAlreadyExistsException` のような専用例外を作り、`@ResponseStatus(HttpStatus.CONFLICT)` を付けることを検討。

---

## Medium

### Issue #7 — JsonParserServiceが単一責任原則に違反している

**Category**: Readability, Best Practice
**Location**: `JsonParserService.java` 全体

**問題**:
クラス内に5つの責務が混在している（JSONデシリアライズ、レスポンス検証、ドキュメントタイプフィルタ、上場企業フィルタ、ドメインモデル構築）。クラス内に `// TODO ここからはJSONパースとは関係ない` というコメントがあることからも作者自身が認識している。

**Improved Version**:
```java
// JsonParserService: JSONパースとバリデーションのみ
@Service
public class JsonParserService {
    public DocumentListResponse parseAndValidate(String jsonData) {
        var response = parseJsonData(jsonData);
        validateResponseStatus(response.getStatus());
        return response;
    }
}

// DocumentMetadataAssembler: ドメインモデル構築に特化
@Service
public class DocumentMetadataAssembler {
    public Optional<ProcessedResponseData> assemble(DocumentListResponse response) {
        if (response.getCount() == 0 || response.getResults() == null) {
            return Optional.empty();
        }
        var filtered = filterPermittedDocumentTypes(response.getResults());
        var withSecCode = filterResultsWithSecCode(filtered);
        if (withSecCode.isEmpty()) return Optional.empty();
        return Optional.of(processResults(withSecCode));
    }
}
```

---

### Issue #8 — AccountService.getAccounts()がXBRL処理毎に全件DBアクセスしている

**Category**: Performance
**Location**: `XbrlFinancialDataExtractor.java` line 57, `AccountService.java`

**問題**:
`extractFinancialDataFromXbrl()` は毎回 `account_master` テーブルを全件フェッチしてMap化している。アカウントマスタはV7マイグレーションで投入される静的データなのに、XBRLファイル処理のたびにDBアクセスが発生している。

**Improved Version**:
```java
// AccountService.java — @Cacheable でキャッシュ化
@Service
public class AccountService {

    @Cacheable("accountMasters")
    public List<AccountMaster> getAccounts() {
        return accountMasterRepository.findAll();
    }
}

// @Configuration クラス
@Configuration
@EnableCaching
public class CacheConfig { }

// application.yml
// spring.cache.type=caffeine
// spring.cache.caffeine.spec=maximumSize=500,expireAfterWrite=1h
```

---

### Issue #9 — DocumentMetadataドメインモデルが不変でない

**Category**: Best Practice (DDD)
**Location**: `domain/model/DocumentMetadata.java`

**問題**:
`FinancialDocument` は final フィールドで完全に不変・コンストラクタでバリデーションしているが、`DocumentMetadata` はフィールドが非final・nullチェックなしと設計が一貫していない。

**Improved Version**:
```java
public class DocumentMetadata {
    private Long id;

    private final DocumentId documentId;
    private final String description;
    private final EdinetCode edinetCode;
    private final EdinetDocumentType documentType;
    private final EdinetFormCode formCode;
    private final LocalDate submissionDate;
    private boolean processed = false; // processedのみ可変

    public DocumentMetadata(DocumentId documentId, String description,
                            EdinetCode edinetCode, EdinetDocumentType documentType,
                            EdinetFormCode formCode, LocalDate submissionDate) {
        Objects.requireNonNull(documentId, "DocumentId cannot be null");
        Objects.requireNonNull(edinetCode, "EdinetCode cannot be null");
        Objects.requireNonNull(documentType, "DocumentType cannot be null");
        this.documentId = documentId;
        // ...
    }
}
```

---

### Issue #10 — EdinetFileExtractionServiceのバッファサイズが非効率

**Category**: Performance
**Location**: `EdinetFileExtractionService.java` line 95

**問題**:
ZIPエントリの読み込みに 1024 バイト（1KB）のバッファを使用している。XBRL/HTML ファイルは一般的に数十KB〜数百KBあり大量のシステムコールが発生する。Java 9+ では `InputStream.transferTo()` が利用可能。

**Current Code**:
```java
private byte[] extractFileContent(ZipInputStream zipIn) throws IOException {
    var out = new ByteArrayOutputStream();
    var buffer = new byte[1024]; // 非効率なバッファサイズ
    int len;
    while ((len = zipIn.read(buffer)) != -1) {
        out.write(buffer, 0, len);
    }
    return out.toByteArray();
}
```

**Improved Version**:
```java
private byte[] extractFileContent(ZipInputStream zipIn) throws IOException {
    var out = new ByteArrayOutputStream();
    zipIn.transferTo(out); // Java 9+ — 内部的に8192バイトバッファを使用
    return out.toByteArray();
}
```

---

## Low

### Issue #11 — length() == 0 を isEmpty() に変えるべき

**Category**: Readability
**Location**: `ApiResponseValidator.java` line 58

**Current Code**:
```java
if (responseBody == null || responseBody.length() == 0) {
```

**Improved Version**:
```java
if (responseBody == null || responseBody.isEmpty()) {
```

---

### Issue #12 — JsonParserServiceTestが@ExtendWith(MockitoExtension.class)を使っていない

**Category**: Best Practice (Testing)
**Location**: `JsonParserServiceTest.java` line 15

**問題**:
プロジェクト全体では `@ExtendWith(MockitoExtension.class)` が標準パターンだが、このクラスだけ `@BeforeEach` 内で `Mockito.mock()` を手動呼び出ししている。

**Current Code**:
```java
class JsonParserServiceTest {
    @BeforeEach
    void setUp() {
        documentService = Mockito.mock(DocumentService.class);
        jsonParserService = new JsonParserService(documentService);
    }
}
```

**Improved Version**:
```java
@ExtendWith(MockitoExtension.class)
class JsonParserServiceTest {

    @Mock
    private DocumentService documentService;

    @InjectMocks
    private JsonParserService jsonParserService;
}
```

---

## Top 3 推奨アクション

1. **Controller依存整理 + `@Deprecated`エンドポイント削除**（Issue #1）
   UseCaseレイヤーの意味を確立し、テストのモック対象を明確にする。

2. **`EdinetContext` の逆方向依存解消**（Issue #2）
   `domain/constants/` パッケージを新設してcontextRef文字列定数を移動し、ドメイン層が外側の層に依存しない構造にする。

3. **N+1クエリを1回のJOINに変える**（Issue #3）
   将来の一括取得API追加に向けた地雷解除。読みやすさとパフォーマンスが同時に向上する。

---

## 良い点

- UseCaseがビジネスロジックを持たないクリーンなオーケストレーターとして機能している
- `DocumentId`・`EdinetCode` の `record` による値オブジェクト実装とバリデーション付きコンストラクタが模範的
- 書き込み=JPA・読み取り=jOOQ の使い分けが全クラスで一貫して守られている
- `XbrlConstants` への XBRL 定数集約が整理されている（Issue #2 を除く）
