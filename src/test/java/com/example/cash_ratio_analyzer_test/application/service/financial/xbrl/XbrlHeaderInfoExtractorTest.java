package com.example.cash_ratio_analyzer_test.application.service.financial.xbrl;

import com.example.cash_ratio_analyzer_test.application.service.dto.HeaderInfo;
import com.example.cash_ratio_analyzer_test.domain.enums.Currency;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class XbrlHeaderInfoExtractorTest {

    private XbrlHeaderInfoExtractor xbrlHeaderInfoExtractor;

    private XbrlDocumentParser xbrlDocumentParser;

    @BeforeEach
    void setUp() throws ParserConfigurationException {
        xbrlDocumentParser = new XbrlDocumentParser();
        xbrlHeaderInfoExtractor = new XbrlHeaderInfoExtractor(xbrlDocumentParser);
    }

    @Test
    @DisplayName("有効なヘッダーコンテンツを使用してヘッダー情報を抽出する")
    void extractHeaderInfo_validHeaderContent() throws IOException {
        var headerContent = Files.readAllBytes(Path.of("src/test/resources/xbrl/test_header_ixbrl.htm"));

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
}