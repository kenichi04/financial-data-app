package com.example.cash_ratio_analyzer_test.service;

import com.example.cash_ratio_analyzer_test.DocumentType;
import org.springframework.stereotype.Service;

@Service
public class EdinetScenarioService {

    private final EdinetDataFetchService edinetDataFetchService;
    private final EdinetDataParsingService edinetDataParsingService;
    private final XbrlParserService xbrlParserService;

    public EdinetScenarioService(EdinetDataFetchService edinetDataFetchService, EdinetDataParsingService edinetDataParsingService, XbrlParserService xbrlParserService) {
        this.edinetDataFetchService = edinetDataFetchService;
        this.edinetDataParsingService = edinetDataParsingService;
        this.xbrlParserService = xbrlParserService;
    }

    public String executeEdinetScenario(String docNumber) {
        var fetchData = edinetDataFetchService.fetchData(DocumentType.XBRL, docNumber);
        // zip形式のデータからターゲットファイルを取得
        // MapかListで返すようにしても良いかも（対象が複数ある場合）
        // 一時ファイル作成も検討する（将来的に）
        var targetData = edinetDataParsingService.extractTargetFile(fetchData);
        // XBRLから必要なデータを抽出
        var extractedData = xbrlParserService.parseXbrl(targetData);

        // TODO DBに保存

        // FIXME 実装途中. 一旦、対象ファイルの内容をそのまま出力しているだけ
        return new String(targetData);
    }
}
