package com.example.cash_ratio_analyzer_test.application.service;

import com.example.cash_ratio_analyzer_test.domain.model.FinancialData;
import com.example.cash_ratio_analyzer_test.DocumentType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EdinetScenarioService {

    private final EdinetDataFetchService edinetDataFetchService;
    private final EdinetDataParsingService edinetDataParsingService;
    private final XbrlParserService xbrlParserService;
    private final FinancialDocumentService financialDocumentService;

    public EdinetScenarioService(EdinetDataFetchService edinetDataFetchService, EdinetDataParsingService edinetDataParsingService, XbrlParserService xbrlParserService, FinancialDocumentService financialDocumentService) {
        this.edinetDataFetchService = edinetDataFetchService;
        this.edinetDataParsingService = edinetDataParsingService;
        this.xbrlParserService = xbrlParserService;
        this.financialDocumentService = financialDocumentService;
    }

    // TODO transactionalアノテーションを付与する
    // 書類一覧APIから書類メタデータ取得、登録する処理を管理する
    public ResponseEntity<String> fetchAndSaveDocumentMetadata() {
        // FIXME 実装途中. 一旦、成功した旨を返すだけ
        return ResponseEntity.ok("success");
    }

    // TODO transactionalアノテーションを付与する
    // 書類取得APIから財務データ取得、分析して登録する処理を管理する
    public ResponseEntity<List<FinancialData>> fetchAndSaveFinancialData(String documentId) {
        var fetchData = edinetDataFetchService.fetchData(DocumentType.XBRL, documentId);
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
