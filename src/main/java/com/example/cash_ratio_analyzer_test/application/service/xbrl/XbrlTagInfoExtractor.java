package com.example.cash_ratio_analyzer_test.application.service.xbrl;

import com.example.cash_ratio_analyzer_test.application.service.constants.XbrlConstants;
import com.example.cash_ratio_analyzer_test.domain.enums.Currency;
import org.springframework.stereotype.Service;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

@Service
public class XbrlTagInfoExtractor {

    private final XbrlDocumentParser xbrlDocumentParser;

    public XbrlTagInfoExtractor(XbrlDocumentParser xbrlDocumentParser) {
        this.xbrlDocumentParser = xbrlDocumentParser;
    }

    // TODO メソッド実装. Javadocコメントも追加
    public void extractTagInfoFromHeaderOrFirstFile(byte[] contentWithTagInfo) {
        var document = xbrlDocumentParser.parseDocumentToDom(contentWithTagInfo);
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

    // TODO コンテキストの定義から当年度の会計年度を取得
    // id例（時点）: CurrentYearInstant_{メンバー要素名}　⇒　periodタグの子要素のinstantタグの値
    // id例（期間）: CurrentYearDuration_{メンバー要素名} ⇒ periodタグの子要素のendDateタグの値
    // 会計期間末（時点）／会計期間（期間）のどちらかが取得できれば良さそう ※対象は`経理の状況`になると思うので.
    private void extractContextFromNodeList(NodeList contextNodeList) {
        for (int i = 0; i < contextNodeList.getLength(); i++) {
            var element = (Element) contextNodeList.item(i);
            var contextId = element.getAttribute(XbrlConstants.ATTRIBUTE_ID);
        }
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
}
