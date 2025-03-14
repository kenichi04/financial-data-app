package com.example.cash_ratio_analyzer_test.application.service.financial.xbrl;

import com.example.cash_ratio_analyzer_test.application.service.AccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class XbrlFinancialDataExtractorTest {

    private XbrlFinancialDataExtractor xbrlFinancialDataExtractor;

    @Mock
    private AccountService accountService;

    private XbrlDocumentParser xbrlDocumentParser;

    @BeforeEach
    void setUp() throws ParserConfigurationException {
        xbrlDocumentParser = new XbrlDocumentParser();
        xbrlFinancialDataExtractor = new XbrlFinancialDataExtractor(accountService, xbrlDocumentParser);
    }

    @Test
    void extractFinancialDataFromXbrl() throws IOException {
        var testContent = Files.readAllBytes(Path.of("src/test/resources/xbrl/test_0105020_content_ixbrl.htm"));

        var financialDataList = xbrlFinancialDataExtractor.extractFinancialDataFromXbrl(testContent);
    }
}