package com.example.cash_ratio_analyzer_test.application.service.xbrl;

import com.example.cash_ratio_analyzer_test.application.service.dto.HeaderInfo;
import com.example.cash_ratio_analyzer_test.domain.enums.Currency;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class XbrlHeaderInfoExtractorTest {

    private XbrlHeaderInfoExtractor xbrlHeaderInfoExtractor;
    private XbrlDocumentParser xbrlDocumentParser;
    private DocumentBuilder documentBuilder;

    @BeforeEach
    void setUp() throws ParserConfigurationException {
        // 依存関係を分離してテストを独立させたいので、XbrlDocumentParserをモック化
        xbrlDocumentParser = Mockito.mock(XbrlDocumentParser.class);
        xbrlHeaderInfoExtractor = new XbrlHeaderInfoExtractor(xbrlDocumentParser);
        // Mockito.when()を使うときに、documentBuilder.parse()を使うために必要
        var factory = DocumentBuilderFactory.newInstance();
        this.documentBuilder = factory.newDocumentBuilder();
    }

    @Test
    void extractHeaderInfo_validHeaderContent() throws IOException {
        var headerContent = Files.readAllBytes(Path.of("src/test/resources/xbrl/test_header_ixbrl.htm"));
        when(xbrlDocumentParser.parseDocumentToDom(headerContent)).thenReturn(parseDocumentToDom(headerContent));

        HeaderInfo headerInfo = xbrlHeaderInfoExtractor.extractHeaderInfo(headerContent);

        // EDINETコード
        assertEquals("E03344", headerInfo.getDeiInfo().get("jpdei_cor:EDINETCodeDEI"));
        // 様式
        assertEquals("第三号様式", headerInfo.getDeiInfo().get("jpdei_cor:DocumentTypeDEI"));
        // 当会計期間終了日
        assertEquals("2024-02-29", headerInfo.getDeiInfo().get("jpdei_cor:CurrentPeriodEndDateDEI"));
        // 当事業年度終了日
        assertEquals("2024-02-29", headerInfo.getDeiInfo().get("jpdei_cor:CurrentFiscalYearEndDateDEI"));
        // 通貨
        assertEquals(Currency.JPY, headerInfo.getCurrency());
    }

    private Document parseDocumentToDom(byte[] xbrlContent) {
        try {
            return documentBuilder.parse(new ByteArrayInputStream(xbrlContent));
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse XBRL content: " + e.getMessage());
        }
    }
}