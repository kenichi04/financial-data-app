package com.example.cash_ratio_analyzer_test.application.service;

import com.example.cash_ratio_analyzer_test.application.service.enums.FetchMode;
import com.example.cash_ratio_analyzer_test.domain.model.FinancialData;
import com.example.cash_ratio_analyzer_test.application.service.enums.FetchDocumentType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

// TODO 2つのメソッドのユースケース違うので、クラス分けても良いかも
@Service
public class EdinetScenarioService {

    private final EdinetDocumentListService edinetDocumentListService;
    private final EdinetDataFetchService edinetDataFetchService;
    private final EdinetDataParsingService edinetDataParsingService;
    private final JsonPerserService jsonPerserService;
    private final XbrlParserService xbrlParserService;
    private final FinancialDocumentService financialDocumentService;

    public EdinetScenarioService(EdinetDocumentListService edinetDocumentListService, EdinetDataFetchService edinetDataFetchService, EdinetDataParsingService edinetDataParsingService, JsonPerserService jsonPerserService, XbrlParserService xbrlParserService, FinancialDocumentService financialDocumentService) {
        this.edinetDocumentListService = edinetDocumentListService;
        this.edinetDataFetchService = edinetDataFetchService;
        this.edinetDataParsingService = edinetDataParsingService;
        this.jsonPerserService = jsonPerserService;
        this.xbrlParserService = xbrlParserService;
        this.financialDocumentService = financialDocumentService;
    }

    // TODO transactionalアノテーションを付与する
    // 書類一覧APIから書類メタデータ取得、登録する処理を管理する
    public ResponseEntity<String> fetchAndSaveDocumentMetadata() {
        // FIXME 実装途中. 一旦、取得したデータを返すだけ
        var data = edinetDocumentListService.fetchDocumentList(FetchMode.METADATA_AND_LIST);
        // TODO companyもここで登録するか？もしくは事前登録しておくか？
        var metadataList = jsonPerserService.parseDocumentList(data);

        // TODO DBに保存

        return ResponseEntity.ok(data);
    }

    // TODO transactionalアノテーションを付与する
    // 書類取得APIから財務データ取得、分析して登録する処理を管理する
    public ResponseEntity<List<FinancialData>> fetchAndSaveFinancialData(String documentId) {
        var fetchData = edinetDataFetchService.fetchFinancialData(FetchDocumentType.XBRL, documentId);
        // zip形式のデータからターゲットファイルを取得
        // MapかListで返すようにしても良いかも（対象が複数ある場合）
        // 一時ファイル作成も検討する（将来的に）
        var targetData = edinetDataParsingService.extractTargetFile(fetchData);
        // XBRLから必要なデータを抽出
        var extractedData = xbrlParserService.parseXbrl(targetData);
        // TODO DBに保存
        financialDocumentService.saveFinancialData(documentId, extractedData);

        // FIXME 実装途中. 一旦、抽出した内容をそのまま出力しているだけ
        return ResponseEntity.ok(extractedData);
    }
}
