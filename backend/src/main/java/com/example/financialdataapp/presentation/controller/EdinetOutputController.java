package com.example.financialdataapp.presentation.controller;

import com.example.financialdataapp.application.service.enums.FetchDocumentType;
import com.example.financialdataapp.application.service.output.EdinetDataOutputService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// Edinetデータ出力テスト用
@Deprecated
@RestController
@RequestMapping("/edinet/output")
public class EdinetOutputController {

    private final EdinetDataOutputService edinetDataOutputService;

    public EdinetOutputController(EdinetDataOutputService edinetDataOutputService) {
        this.edinetDataOutputService = edinetDataOutputService;
    }

    @GetMapping("/xbrl/zip/{documentNumber}")
    public String getXbrlZipData(@PathVariable String documentNumber) {
        return edinetDataOutputService.testFetchEdinetZipData(FetchDocumentType.XBRL, documentNumber);
    }

    @GetMapping("/csv/zip/{documentNumber}")
    public String getCsvZipData(@PathVariable String documentNumber) {
        return edinetDataOutputService.testFetchEdinetZipData(FetchDocumentType.CSV, documentNumber);
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
