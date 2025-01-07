package com.example.cash_ratio_analyzer_test.controller;

import com.example.cash_ratio_analyzer_test.entity.FinancialData;
import com.example.cash_ratio_analyzer_test.service.EdinetScenarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/edinet")
public class EdinetController {

    private final EdinetScenarioService edinetScenarioService;

    public EdinetController(EdinetScenarioService edinetScenarioService) {
        this.edinetScenarioService = edinetScenarioService;
    }

    @GetMapping("/fetchAndAnalyze/{documentNumber}")
    public ResponseEntity<List<FinancialData>> fetchAndAnalyzeData(@PathVariable String documentNumber) {
        // シナリオサービスを呼び出す
        return edinetScenarioService.executeEdinetScenario(documentNumber);
    }
}
