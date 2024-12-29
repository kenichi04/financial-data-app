package com.example.cash_ratio_analyzer_test.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import com.example.cash_ratio_analyzer_test.service.EdinetService;

@RestController
public class EdinetController {

    private final EdinetService edinetService;

    public EdinetController(EdinetService edinetService) {
        this.edinetService = edinetService;
    }

    @GetMapping("/xbrl/{docNumber}")
    public String getXbrlData(@PathVariable String docNumber) {
//        String testDocNumber = "S100QIM7";
        return edinetService.testFetchEdinetXbrlData(docNumber);
    }
    @GetMapping("/pdf/{docNumber}")
    public String getPdfData(@PathVariable String docNumber) {
        return edinetService.testFetchEdinetPdfData(docNumber);
    }
}
