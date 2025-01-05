package com.example.cash_ratio_analyzer_test.service;

import com.example.cash_ratio_analyzer_test.DocumentType;
import com.example.cash_ratio_analyzer_test.repository.IEdinetDataRepository;
import org.springframework.stereotype.Service;

@Service
public class EdinetScenarioService {

    private final EdinetDataFetchService edinetDataFetchService;
    private final EdinetDataParsingService edinetDataParsingService;
    private final XbrlParserService xbrlParserService;
    private final IEdinetDataRepository edinetDataRepository;

    public EdinetScenarioService(EdinetDataFetchService edinetDataFetchService, EdinetDataParsingService edinetDataParsingService, XbrlParserService xbrlParserService, IEdinetDataRepository edinetDataRepository) {
        this.edinetDataFetchService = edinetDataFetchService;
        this.edinetDataParsingService = edinetDataParsingService;
        this.xbrlParserService = xbrlParserService;
        this.edinetDataRepository = edinetDataRepository;
    }

    public String executeEdinetScenario(String documentNumber) {
        var fetchData = edinetDataFetchService.fetchData(DocumentType.XBRL, documentNumber);
        // zip形式のデータからターゲットファイルを取得
        // MapかListで返すようにしても良いかも（対象が複数ある場合）
        // 一時ファイル作成も検討する（将来的に）
        var targetData = edinetDataParsingService.extractTargetFile(fetchData);
        // XBRLから必要なデータを抽出
        var extractedData = xbrlParserService.parseXbrl(targetData);

        // TODO DBに保存（一旦インタフェース実装してインメモリに保存する）
        edinetDataRepository.save(extractedData);

        // FIXME 実装途中. 一旦、対象ファイルの内容をそのまま出力しているだけ（レスポンスエンティティ返すよう変更）
        return new String(targetData);
    }
}
