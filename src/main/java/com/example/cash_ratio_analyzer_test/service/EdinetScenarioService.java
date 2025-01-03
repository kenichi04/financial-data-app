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
        var targetData = edinetDataParsingService.extractTargetFile(fetchData);

        // TODO 実装途中. 一旦、対象ファイルの内容をそのまま出力しているだけ
        try {
            return new String(targetData, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }
}
