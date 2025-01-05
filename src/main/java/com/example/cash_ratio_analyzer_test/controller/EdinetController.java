package com.example.cash_ratio_analyzer_test.controller;

import com.example.cash_ratio_analyzer_test.DocumentType;
import com.example.cash_ratio_analyzer_test.service.EdinetDataOutputService;
import com.example.cash_ratio_analyzer_test.service.EdinetScenarioService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/edinet")
public class EdinetController {

    private final EdinetScenarioService edinetScenarioService;
    private final EdinetDataOutputService edinetDataOutputService;

    public EdinetController(EdinetScenarioService edinetScenarioService, EdinetDataOutputService edinetDataOutputService) {
        this.edinetScenarioService = edinetScenarioService;
        this.edinetDataOutputService = edinetDataOutputService;
    }

    @GetMapping("/fetchAndAnalyze/{documentNumber}")
    public String fetchAndAnalyzeData(@PathVariable String documentNumber) {
        // シナリオサービスを呼び出す
        return edinetScenarioService.executeEdinetScenario(documentNumber);
    }

    @GetMapping("/xbrl/zip/{documentNumber}")
    public String getXbrlZipData(@PathVariable String documentNumber) {
//        String testDocumentNumber = "S100TGZR";
        return edinetDataOutputService.testFetchEdinetZipData(DocumentType.XBRL, documentNumber);
    }

    @GetMapping("/csv/zip/{documentNumber}")
    public String getCsvZipData(@PathVariable String documentNumber) {
        return edinetDataOutputService.testFetchEdinetZipData(DocumentType.CSV, documentNumber);
    }

    @GetMapping("/xbrl/{documentNumber}")
    public String getXbrlData(@PathVariable String documentNumber) {
        return edinetDataOutputService.testFetchEdinetXbrlData(documentNumber);
    }

    @GetMapping("/pdf/{documentNumber}")
    public String getPdfData(@PathVariable String documentNumber) {
        return edinetDataOutputService.testFetchEdinetPdfData(documentNumber);
    }
}
