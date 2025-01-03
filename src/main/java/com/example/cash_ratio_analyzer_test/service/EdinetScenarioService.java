package com.example.cash_ratio_analyzer_test.service;

import com.example.cash_ratio_analyzer_test.DocumentType;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;

@Service
public class EdinetScenarioService {

    private final EdinetDataFetchService edinetDataFetchService;
    private final EdinetDataParsingService edinetDataParsingService;

    public EdinetScenarioService(EdinetDataFetchService edinetDataFetchService, EdinetDataParsingService edinetDataParsingService) {
        this.edinetDataFetchService = edinetDataFetchService;
        this.edinetDataParsingService = edinetDataParsingService;
    }

    public String executeEdinetScenario(String docNumber) {
        var fetchData = edinetDataFetchService.fetchData(DocumentType.XBRL, docNumber);
        // zipファイルからターゲットファイルのみ抽出
        // Mapで返すようにしても良いかも
        // 一時ファイル作成した方が良いか？
        var targetData = edinetDataParsingService.extractTargetFileAsString(fetchData);

        // TODO 必要なデータ抽出する
        // TODO DBに保存

        // FIXME 実装途中. 一旦、対象ファイルの内容をそのまま出力しているだけ
        return targetData;
    }
}
