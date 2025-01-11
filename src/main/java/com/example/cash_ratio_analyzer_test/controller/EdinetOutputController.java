package com.example.cash_ratio_analyzer_test.controller;

import com.example.cash_ratio_analyzer_test.DocumentType;
import com.example.cash_ratio_analyzer_test.application.service.EdinetDataOutputService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/edinet/output")
public class EdinetOutputController {

    private final EdinetDataOutputService edinetDataOutputService;

    public EdinetOutputController(EdinetDataOutputService edinetDataOutputService) {
        this.edinetDataOutputService = edinetDataOutputService;
    }

    @GetMapping("/xbrl/zip/{documentNumber}")
    public String getXbrlZipData(@PathVariable String documentNumber) {
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
