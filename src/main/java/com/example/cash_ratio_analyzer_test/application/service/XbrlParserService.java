package com.example.cash_ratio_analyzer_test.application.service;

import com.example.cash_ratio_analyzer_test.domain.enums.Currency;
import com.example.cash_ratio_analyzer_test.domain.model.FinancialData;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

// TODO インターフェース作成して、切替可能にする
/**
 * EDINET書類取得APIレスポンスから抽出したXBRLコンテンツを解析するサービスクラス。
 */
@Service
public class XbrlParserService {

    private final AccountService accountService;

    private DocumentBuilder documentBuilder;

    public XbrlParserService(AccountService accountService) throws ParserConfigurationException {
        this.accountService = accountService;
        var factory = DocumentBuilderFactory.newInstance();
        this.documentBuilder = factory.newDocumentBuilder();
    }

    /**
     * XBRLコンテンツを解析し、FinancialDataのリストを返します。
     *
     * @param xbrlContent 解析するXBRLコンテンツのバイト配列
     * @return 解析されたFinancialDataのリスト
     */
    // TODO SAXパーサーで逐次解析の方がメモリ効率が良い
    public List<FinancialData> parseXbrl(byte[] xbrlContent) {
        var result = new ArrayList<FinancialData>();

        var document = parseDocumentToDom(xbrlContent);
        var elements = document.getDocumentElement();
        // TODO タグについては要検討 > 一旦ix:nonFractionでよさそう （enum、定数で管理？）
        // ix:nonFraction: 金額又は数値を表現する場合
        // 参考: https://www.fsa.go.jp/search/20151228/2b_1.pdf
        var nodeList = elements.getElementsByTagName("ix:nonFraction");

        if (nodeList.getLength() == 0) {
            return result;
        }


        // TODO ここで取得する情報（科目）を選別する > accountsテーブルに取得する科目をマスタデータとして登録しておく想定
        // 企業データは登録済の想定, 企業に紐づく文書データは別APIで事前登録済の想定
        // ここで保存する科目は、登録済の文書データに紐づく必要がある
        for (int i = 0; i < nodeList.getLength(); i++) {
            var element = (Element) nodeList.item(i);
            var financialData = extractFinancialDataFromElement(element);

            if (financialData.isEmpty()) {
                continue;
            }
            result.add(financialData.get());
        }

        return result;
    }

    /**
     * 指定されたXML要素からFinancialDataを抽出します。
     *
     * @param element XML要素
     * @return 抽出されたFinancialData、該当するデータが無い場合はOptional.empty()
     */
    private Optional<FinancialData> extractFinancialDataFromElement(Element element) {
        var accountMap = accountService.getAccounts().stream()
                .collect(Collectors.toMap(account -> account.getCode(), account -> account));

        // `jppfs_cor:`は名前空間プレフィックスのため不要
        // 財務諸表本表タクソノミの語彙スキーマの名前空間宣言
        var name = element.getAttribute("name").replace("jppfs_cor:", "");
        if (name.isEmpty() || !accountMap.containsKey(name)) {
            return Optional.empty();
        }

        var contextRef = element.getAttribute("contextRef");
        var unitRef = element.getAttribute("unitRef");
        var value = extractValueFromElement(element);
        // 金額が0の場合は登録しない
        if (value.equals(BigDecimal.ZERO)) {
            return Optional.empty();
        }

        var account = accountMap.get(name);
        return Optional.of(new FinancialData(account, contextRef, value, Currency.fromCode(unitRef)));
    }

    /**
     * 指定されたXML要素から数値を抽出します。
     *
     * @param element XML要素
     * @return 抽出された数値、数値が無い場合はBigDecimal.ZERO
     */
    private BigDecimal extractValueFromElement(Element element) {
        try {
            // 金額はカンマ区切り
            return new BigDecimal(element.getTextContent().trim().replace(",", ""));
        } catch (NumberFormatException e) {
            return BigDecimal.ZERO;
        }
    }

    /**
     * XBRLコンテンツをDOMドキュメントに解析します。
     *
     * @param xbrlContent 解析するXBRLコンテンツのバイト配列
     * @return 解析されたDOMドキュメント
     * @throws RuntimeException XBRLコンテンツの解析に失敗した場合
     */
    private Document parseDocumentToDom(byte[] xbrlContent) {
        try {
            return documentBuilder.parse(new ByteArrayInputStream(xbrlContent));
        } catch (SAXException | IOException e) {
            throw new RuntimeException("Failed to parse XBRL content: " + e.getMessage());
        }
    }
}
