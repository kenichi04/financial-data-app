package com.example.cash_ratio_analyzer_test.controller;

import com.example.cash_ratio_analyzer_test.DocumentType;
import com.example.cash_ratio_analyzer_test.service.EdinetDataOutputService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EdinetController {

    private final EdinetDataOutputService edinetDataOutputService;

    public EdinetController(EdinetDataOutputService edinetDataOutputService) {
        this.edinetDataOutputService = edinetDataOutputService;
    }

    @GetMapping("/xbrl/zip/{docNumber}")
    public String getXbrlZipData(@PathVariable String docNumber) {
//        String testDocNumber = "S100TGZR";
        return edinetDataOutputService.testFetchEdinetZipData(DocumentType.XBRL, docNumber);
    }

    @GetMapping("/csv/zip/{docNumber}")
    public String getCsvZipData(@PathVariable String docNumber) {
        return edinetDataOutputService.testFetchEdinetZipData(DocumentType.CSV, docNumber);
    }

    @GetMapping("/xbrl/{docNumber}")
    public String getXbrlData(@PathVariable String docNumber) {
        return edinetDataOutputService.testFetchEdinetXbrlData(docNumber);
    }

    @GetMapping("/pdf/{docNumber}")
    public String getPdfData(@PathVariable String docNumber) {
        return edinetDataOutputService.testFetchEdinetPdfData(docNumber);
    }
}
