package com.example.cash_ratio_analyzer_test.application.service;

import com.example.cash_ratio_analyzer_test.application.service.constants.XbrlConstants;
import com.example.cash_ratio_analyzer_test.domain.enums.Currency;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.ByteArrayInputStream;

@Service
public class XbrlTagInfoExtractor {

    private DocumentBuilder documentBuilder;

    public XbrlTagInfoExtractor() throws ParserConfigurationException {
        var factory = DocumentBuilderFactory.newInstance();
        this.documentBuilder = factory.newDocumentBuilder();
    }

    // TODO メソッド実装. Javadocコメントも追加
    public void extractTagInfoFromHeaderOrFirstFile(byte[] contentWithTagInfo) {
        var document = parseDocumentToDom(contentWithTagInfo);
        var elements = document.getDocumentElement();
        var headerNodeList = elements.getElementsByTagName(XbrlConstants.IX_HEADER);

        // headerタグは1つの想定
        if (headerNodeList.getLength() == 0 || headerNodeList.getLength() != 1) {
            throw new RuntimeException("Failed to parse XBRL content: header tag not found");
        }
        var headerNode = (Element) headerNodeList.item(0);

        // TODO コンテキストIDタグの情報を取得して、instantから期末日を取得できる
        // <xbrli:context id="CurrentYearInstant"><xbrli:instant>2024-02-29</xbrli:instant></xbrli:context>

        var contextNodeList = headerNode.getElementsByTagName(XbrlConstants.XBRLI_CONTEXT);

        Currency currency = extractCurrencyFromUnitNodeList(
                headerNode.getElementsByTagName(XbrlConstants.XBRLI_UNIT));

        // TODO FinancialDocument生成して返す. もしくは、FinancialDocument生成に必要なメタ情報を返す. メタ情報クラス作る方が疎結合になるかも
    }

    /**
     * 指定された単位ノードリストから通貨を抽出します。
     *
     * @param unitNodeList 単位ノードリスト
     * @return 抽出された通貨
     * @throws RuntimeException 通貨コードが見つからない場合
     */
    private Currency extractCurrencyFromUnitNodeList(NodeList unitNodeList) {
        for (int i = 0; i < unitNodeList.getLength(); i++) {
            var element = (Element) unitNodeList.item(i);
            var unitId = element.getAttribute(XbrlConstants.ATTRIBUTE_ID);
            var currency = getCurrencyFromUnitId(unitId);
            if (currency == null) {
                continue;
            }
            // TODO 一つしかないと思うので、最初の要素を取得で良いはず？
            var measureNode = element.getElementsByTagName(XbrlConstants.XBRLI_MEASURE).item(0);
            var measure = measureNode.getTextContent();
            // ISOプレフィックスを除いた値がJPY or USDになる想定
            var currencyCode = measure.replace(XbrlConstants.UNIT_ISO4217, "");
            if (currency.getCode().equals(currencyCode)) {
                return currency;
            }
        }
        throw new RuntimeException("Failed to parse XBRL content: currency code not found");
    }

    /**
     * 指定された単位IDから通貨を取得します。
     *
     * @param unitId 単位ID
     * @return 通貨、該当する通貨が無い場合はnull
     */
    private Currency getCurrencyFromUnitId(String unitId) {
        try {
            return Currency.fromCode(unitId);
        } catch (IllegalArgumentException e) {
            return null;
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
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse XBRL content: " + e.getMessage());
        }
    }

}
