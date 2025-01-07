package com.example.cash_ratio_analyzer_test.service;

import com.example.cash_ratio_analyzer_test.DocumentType;
import com.example.cash_ratio_analyzer_test.entity.FinancialData;
import com.example.cash_ratio_analyzer_test.repository.IFinancialDataRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EdinetScenarioService {

    private final EdinetDataFetchService edinetDataFetchService;
    private final EdinetDataParsingService edinetDataParsingService;
    private final XbrlParserService xbrlParserService;
    private final IFinancialDataRepository financialDataRepository;

    public EdinetScenarioService(EdinetDataFetchService edinetDataFetchService, EdinetDataParsingService edinetDataParsingService, XbrlParserService xbrlParserService, IFinancialDataRepository edinetDataRepository) {
        this.edinetDataFetchService = edinetDataFetchService;
        this.edinetDataParsingService = edinetDataParsingService;
        this.xbrlParserService = xbrlParserService;
        this.financialDataRepository = edinetDataRepository;
    }

    // transactionalアノテーションを付与する
    public ResponseEntity<List<FinancialData>> executeEdinetScenario(String documentNumber) {
        var fetchData = edinetDataFetchService.fetchData(DocumentType.XBRL, documentNumber);
        // zip形式のデータからターゲットファイルを取得
        // MapかListで返すようにしても良いかも（対象が複数ある場合）
        // 一時ファイル作成も検討する（将来的に）
        var targetData = edinetDataParsingService.extractTargetFile(fetchData);
        // XBRLから必要なデータを抽出
        var extractedData = xbrlParserService.parseXbrl(targetData);
        // TODO DBに保存（FinancialDataService経由が良いかも）
        financialDataRepository.save(extractedData);

        // FIXME 実装途中. 一旦、抽出した内容をそのまま出力しているだけ
        return ResponseEntity.ok(extractedData);
    }
}
