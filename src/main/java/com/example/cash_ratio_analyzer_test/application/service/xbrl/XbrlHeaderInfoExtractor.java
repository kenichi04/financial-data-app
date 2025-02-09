package com.example.cash_ratio_analyzer_test.application.service.xbrl;

import com.example.cash_ratio_analyzer_test.application.service.constants.XbrlConstants;
import com.example.cash_ratio_analyzer_test.application.service.dto.HeaderInfo;
import com.example.cash_ratio_analyzer_test.domain.enums.Currency;
import org.springframework.stereotype.Service;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.util.HashMap;
import java.util.Map;

/**
 * XBRLヘッダー情報を抽出するサービスクラス。
 */
@Service
public class XbrlHeaderInfoExtractor {

    private final XbrlDocumentParser xbrlDocumentParser;

    public XbrlHeaderInfoExtractor(XbrlDocumentParser xbrlDocumentParser) {
        this.xbrlDocumentParser = xbrlDocumentParser;
    }

    /**
     * ヘッダー情報を抽出します。
     *
     * @param headerContent ヘッダーコンテンツのバイト配列
     * @return 抽出されたヘッダー情報
     * @throws RuntimeException ヘッダータグが見つからない場合
     */
    public HeaderInfo extractHeaderInfo(byte[] headerContent) {
        var document = xbrlDocumentParser.parseDocumentToDom(headerContent);
        var elements = document.getDocumentElement();
        var headerNodeList = elements.getElementsByTagName(XbrlConstants.IX_HEADER);

        // headerタグは1つの想定
        if (headerNodeList.getLength() != 1) {
            throw new RuntimeException("Failed to parse XBRL content: header tag not found");
        }
        var headerNode = (Element) headerNodeList.item(0);

        // hiddenNodeの一つがDEI情報
        var hiddenNodeList = headerNode.getElementsByTagName(XbrlConstants.IX_HIDDEN);
        var deiMap = extractDeiInfoFromHiddenNodeList(hiddenNodeList);

        var currency = extractCurrencyFromUnitNodeList(
                headerNode.getElementsByTagName(XbrlConstants.XBRLI_UNIT));

        return new HeaderInfo(deiMap, currency);
    }

    /**
     * hiddenNodeListからDEI情報を抽出します。
     *
     * @param hiddenNodeList DEI情報を含む可能性のあるhiddenノードリスト
     * @return DEI情報のマップ
     * @throws RuntimeException DEI情報が見つからない場合
     */
    public Map<String, String> extractDeiInfoFromHiddenNodeList(NodeList hiddenNodeList) {
        final String edinetCodeAttribute = XbrlConstants.JP_DEI_NAMESPACE + "EDINETCodeDEI";
        var deiMap = new HashMap<String, String>();
        // TODO 二重ループはなるべく使いたくないが...
        for (int i = 0; i < hiddenNodeList.getLength(); i++) {
            var hiddenNode = (Element) hiddenNodeList.item(i);
            var nonNumericNodeList = hiddenNode.getElementsByTagName(XbrlConstants.IX_NON_NUMERIC);
            for (int j = 0; j < nonNumericNodeList.getLength(); j++) {
                var node = (Element) nonNumericNodeList.item(j);
                var key = node.getAttribute(XbrlConstants.ATTRIBUTE_NAME);
                var value = node.getTextContent();
                deiMap.put(key, value);
            }
            // edinetCodeが取得できていれば、DEI情報を含むhiddenタグは取得済と判定
            if (deiMap.containsKey(edinetCodeAttribute)) {
                break;
            }
        }
        if (!deiMap.containsKey(edinetCodeAttribute)) {
            throw new RuntimeException("Failed to parse XBRL content: DEI node not found");
        }
        return deiMap;
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

            var measureNodeList = element.getElementsByTagName(XbrlConstants.XBRLI_MEASURE);
            if (measureNodeList.getLength() != 1) {
                throw new RuntimeException("Failed to parse XBRL content: measure tag not found");
            }
            var measureNode = measureNodeList.item(0);
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
