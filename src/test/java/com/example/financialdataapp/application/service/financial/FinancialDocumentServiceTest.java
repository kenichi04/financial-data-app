package com.example.financialdataapp.application.service.financial;

import com.example.financialdataapp.application.service.constants.XbrlConstants;
import com.example.financialdataapp.application.service.dto.HeaderInfo;
import com.example.financialdataapp.application.service.metadata.DocumentMetadataService;
import com.example.financialdataapp.domain.enums.Balance;
import com.example.financialdataapp.domain.enums.Currency;
import com.example.financialdataapp.domain.enums.DisplayScale;
import com.example.financialdataapp.domain.model.*;
import com.example.financialdataapp.domain.model.context.EdinetContext;
import com.example.financialdataapp.domain.repository.IFinancialDocumentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FinancialDocumentServiceTest {

    @Mock
    private DocumentMetadataService documentMetadataService;

    @Mock
    private IFinancialDocumentRepository financialDocumentRepository;

    @InjectMocks
    private FinancialDocumentService financialDocumentService;

    @Test
    void createFinancialDocument_Success() {
        // given
        var documentId = "TEST0001";
        var headerInfo = createTestHeaderInfo();
        var financialDataList = List.of(createTestFinancialData());

        doNothing().when(documentMetadataService).updateMetadataProcessedStatus(any());
        doNothing().when(financialDocumentRepository).create(any());

        // when
        var result = financialDocumentService.createFinancialDocument(documentId, headerInfo, financialDataList);

        // then
        assertEquals(new DocumentId(documentId), result);

        // create()の引数として期待値を持つFinancialDocumentオブジェクトが渡されたことを検証
        ArgumentCaptor<FinancialDocument> docCaptor = ArgumentCaptor.forClass(FinancialDocument.class);
        verify(financialDocumentRepository, times(1)).create(docCaptor.capture());
        verify(documentMetadataService, times(1)).updateMetadataProcessedStatus(any());

        FinancialDocument createdDoc = docCaptor.getValue();
        assertEquals(new DocumentId(documentId), createdDoc.getDocumentId());
        assertEquals(new EdinetCode("TEST01"), createdDoc.getEdinetCode());
    }

    @Test
    void createFinancialDocument_ShouldThrowException_WhenEdinetCodeIsMissing() {
        var documentId = "TEST0001";
        var invalidHeaderInfo = new HeaderInfo(
                Map.of(XbrlConstants.DEI_ATTRIBUTE_DOCUMENT_TYPE, "テスト様式",
                        XbrlConstants.DEI_ATTRIBUTE_CURRENT_PERIOD_END_DATE, "2024-02-29"),
                Currency.JPY);
        var financialDataList = List.of(createTestFinancialData());

        assertThrows(IllegalArgumentException.class,
                () -> financialDocumentService.createFinancialDocument(documentId, invalidHeaderInfo, financialDataList));
    }

    @Test
    void createFinancialDocument_ShouldThrowException_WhenDocumentTypeIsMissing() {
        var documentId = "TEST0001";
        var invalidHeaderInfo = new HeaderInfo(
                Map.of(XbrlConstants.DEI_ATTRIBUTE_EDINET_CODE, "TEST01",
                        XbrlConstants.DEI_ATTRIBUTE_CURRENT_PERIOD_END_DATE, "2024-02-29"),
                Currency.JPY);
        var financialDataList = List.of(createTestFinancialData());

        assertThrows(IllegalArgumentException.class,
                () -> financialDocumentService.createFinancialDocument(documentId, invalidHeaderInfo, financialDataList));
    }

    @Test
    void createFinancialDocument_ShouldThrowException_WhenPeriodIsMissing() {
        var documentId = "TEST0001";
        var invalidHeaderInfo = new HeaderInfo(
                Map.of(XbrlConstants.DEI_ATTRIBUTE_EDINET_CODE, "TEST01",
                        XbrlConstants.DEI_ATTRIBUTE_DOCUMENT_TYPE, "テスト様式"),
                Currency.JPY);
        var financialDataList = List.of(createTestFinancialData());

        assertThrows(IllegalArgumentException.class,
                () -> financialDocumentService.createFinancialDocument(documentId, invalidHeaderInfo, financialDataList));
    }

    private HeaderInfo createTestHeaderInfo() {
        var deiInfo = Map.of(
                XbrlConstants.DEI_ATTRIBUTE_EDINET_CODE, "TEST01",
                XbrlConstants.DEI_ATTRIBUTE_DOCUMENT_TYPE, "テスト様式",
                XbrlConstants.DEI_ATTRIBUTE_CURRENT_PERIOD_END_DATE, "2024-02-29");
        return new HeaderInfo(
                deiInfo, Currency.JPY);
    }

    private FinancialData createTestFinancialData() {
        var account = new AccountMaster(
                1L, "CashAndDeposits",
                "現金預金", "Cash and deposits",
                Balance.DEBIT);

        var edinetContext = EdinetContext.fromContextRef("CurrentYearInstant_NonConsolidatedMember");

        return new FinancialData(
                account.getId(),
                edinetContext,
                new BigDecimal("100"),
                DisplayScale.MILLION_YEN,
                Currency.JPY);
    }
}