package com.example.cash_ratio_analyzer_test.application.service;

import com.example.cash_ratio_analyzer_test.application.service.constants.XbrlConstants;
import com.example.cash_ratio_analyzer_test.domain.enums.Currency;
import com.example.cash_ratio_analyzer_test.domain.model.Account;
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
import java.util.Map;
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

    // TODO メソッド実装. Javadocコメントも追加
    public void extractTagInfoFromHeaderOrFirstFile(byte[] contentWithTagInfo) {
        var document = parseDocumentToDom(contentWithTagInfo);
        var elements = document.getDocumentElement();
        var nodeList = elements.getElementsByTagName(XbrlConstants.IX_HEADER);

        if (nodeList.getLength() == 0) {
            return;
        }

        // TODO IX_HEADERタグは1つしかないと思うので、for文は不要？
        for (int i = 0; i < nodeList.getLength(); i++) {
            var element = (Element) nodeList.item(i);

            // TODO コンテキストIDタグの情報を取得して、instantから期末日を取得できる
            // <xbrli:context id="CurrentYearInstant"><xbrli:instant>2024-02-29</xbrli:instant></xbrli:context>
            // TODO ユニットIDの情報を取得して、Currencyを登録する. 一旦JPYとUSDのみ対応
            // 日本円：ユニットID：JPY<xbrli:unit id="JPY"> 、measure要素：iso4217:JPYになる
            // 米ドル：ユニットID：USD<xbrli:unit id="USD"> 、measure要素：iso4217:USDになるもよう
        }
    }

    /**
     * XBRLコンテンツを解析し、FinancialDataのリストを返します。
     *
     * @param xbrlContent 解析するXBRLコンテンツのバイト配列
     * @return 解析されたFinancialDataのリスト
     */
    // TODO SAXパーサーで逐次解析の方がメモリ効率が良い
    public List<FinancialData> extractFinancialDataFromXbrl(byte[] xbrlContent) {
        var result = new ArrayList<FinancialData>();

        var document = parseDocumentToDom(xbrlContent);
        var elements = document.getDocumentElement();
        // TODO タグについては要検討 > 一旦ix:nonFractionでよさそう(nonFraction以外も使う場合はenumで管理)
        var nodeList = elements.getElementsByTagName(XbrlConstants.IX_NON_FRACTION);

        if (nodeList.getLength() == 0) {
            return result;
        }

        // 取得するFinancialDataを選別するための科目マップ. accountsテーブルに対象となる科目をマスタデータとして登録しておく想定
        var accountMap = accountService.getAccounts().stream()
                .collect(Collectors.toMap(account -> account.getCode(), account -> account));

        // ここで保存するデータは、登録済の文書メタデータに紐づく必要がある
        for (int i = 0; i < nodeList.getLength(); i++) {
            var element = (Element) nodeList.item(i);
            var financialData = extractFinancialDataFromElement(element, accountMap);

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
     * @param accountMap 科目マップ
     * @return 抽出されたFinancialData、該当するデータが無い場合はOptional.empty()
     */
    private Optional<FinancialData> extractFinancialDataFromElement(Element element, Map<String, Account> accountMap) {
        // `jppfs_cor:`は名前空間プレフィックスのため不要
        var name = element.getAttribute(XbrlConstants.ATTRIBUTE_NAME)
                .replace(XbrlConstants.JP_PFS_COR_NAMESPACE_PREFIX, "");
        if (name.isEmpty() || !accountMap.containsKey(name)) {
            return Optional.empty();
        }

        var contextRef = element.getAttribute(XbrlConstants.ATTRIBUTE_CONTEXT_REF);
        var unitRef = element.getAttribute(XbrlConstants.ATTRIBUTE_UNIT_REF);
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
            return new BigDecimal(element.getTextContent().trim()
                    .replace(XbrlConstants.COMMA, ""));
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
