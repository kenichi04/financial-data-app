package com.example.financialdataapp.application.service.financial.xbrl;

import com.example.financialdataapp.application.service.AccountService;
import com.example.financialdataapp.domain.enums.Balance;
import com.example.financialdataapp.domain.model.AccountMaster;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.xml.parsers.ParserConfigurationException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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

    // TODO マイナスの値が取れることもテストする
    @Test
    void extractFinancialDataFromXbrl() throws IOException {
        // given
        var testContent = Files.readAllBytes(Path.of("src/test/resources/xbrl/test_0105020_content_ixbrl.htm"));

        var account  = new AccountMaster(
                1L, "CashAndDeposits", "現金預金", "Cash and deposits", Balance.DEBIT);
        when(accountService.getAccounts()).thenReturn(List.of(account));

        // when
        var financialDataList = xbrlFinancialDataExtractor.extractFinancialDataFromXbrl(testContent);

        // then
        assertNotNull(financialDataList);
        assertEquals(2, financialDataList.size(), "前期と今期のデータが取得できているか");

        var data1 = financialDataList.get(0);
        var data2 = financialDataList.get(1);

        assertEquals(1L, data1.getAccountId());
        assertNotNull(data1.getAmount());

        verify(accountService, times(1)).getAccounts();
    }
}