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
        xbrlDocumentParser = Mockito.mock(XbrlDocumentParser.class);
        xbrlHeaderInfoExtractor = new XbrlHeaderInfoExtractor(xbrlDocumentParser);
        // Mockito.when()を使うときに、documentBuilder.parse()を使うために必要
        var factory = DocumentBuilderFactory.newInstance();
        this.documentBuilder = factory.newDocumentBuilder();
    }

    @Test
    void extractHeaderInfo_validHeaderContent() throws IOException {
        var headerContent = Files.readAllBytes(Path.of("src/test/resources/test-header.xml"));
        when(xbrlDocumentParser.parseDocumentToDom(headerContent)).thenReturn(parseDocumentToDom(headerContent));

        HeaderInfo headerInfo = xbrlHeaderInfoExtractor.extractHeaderInfo(headerContent);

        assertNotNull(headerInfo);
        assertEquals("E12345", headerInfo.getDeiInfo().get("jpdei_cor:EDINETCodeDEI"));
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