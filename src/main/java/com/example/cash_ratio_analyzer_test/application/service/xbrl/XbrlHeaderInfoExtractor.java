package com.example.cash_ratio_analyzer_test.application.service.xbrl;

import com.example.cash_ratio_analyzer_test.application.service.constants.XbrlConstants;
import com.example.cash_ratio_analyzer_test.domain.enums.Currency;
import org.springframework.stereotype.Service;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

@Service
public class XbrlHeaderInfoExtractor {

    private final XbrlDocumentParser xbrlDocumentParser;

    public XbrlHeaderInfoExtractor(XbrlDocumentParser xbrlDocumentParser) {
        this.xbrlDocumentParser = xbrlDocumentParser;
    }

    // TODO メソッド実装. Javadocコメントも追加
    public void extractTagInfoFromHeaderOrFirstFile(byte[] contentWithTagInfo) {
        var document = xbrlDocumentParser.parseDocumentToDom(contentWithTagInfo);
        var elements = document.getDocumentElement();
        var headerNodeList = elements.getElementsByTagName(XbrlConstants.IX_HEADER);

        // headerタグは1つの想定
        if (headerNodeList.getLength() != 1) {
            throw new RuntimeException("Failed to parse XBRL content: header tag not found");
        }
        var headerNode = (Element) headerNodeList.item(0);

        // TODO 期間はDEIから取得する
        var period = extractContextFromNodeList(
                headerNode.getElementsByTagName(XbrlConstants.XBRLI_CONTEXT));
        var currency = extractCurrencyFromUnitNodeList(
                headerNode.getElementsByTagName(XbrlConstants.XBRLI_UNIT));

        // TODO FinancialDocument生成して返す. もしくは、FinancialDocument生成に必要なメタ情報を返す. メタ情報クラス作る方が疎結合になるかも
    }

    /**
     * 指定されたコンテキストノードリストから期間情報を抽出します。
     *
     * @param contextNodeList コンテキストノードリスト
     * @return 抽出された期間情報
     * @throws RuntimeException 期間情報が見つからない場合
     */
    // 実装が良くないので、使用する場合はテストコード書いた後にリファクタリングする
    // 期間はDEIから取得するため、このメソッドは使用しない
    @Deprecated
    private String extractContextFromNodeList(NodeList contextNodeList) {
        for (int i = 0; i < contextNodeList.getLength(); i++) {
            var element = (Element) contextNodeList.item(i);
            var contextId = element.getAttribute(XbrlConstants.ATTRIBUTE_ID);
            // context要素は複数あるため、特定のコンテキストIDのみ次処理へ
            if (!XbrlConstants.CONTEXT_CURRENT_YEAR_INSTANT.equals(contextId)
                    && !XbrlConstants.CONTEXT_CURRENT_YEAR_DURATION.equals(contextId)
                    && !XbrlConstants.CONTEXT_CURRENT_YEAR_INSTANT_NON_CONSOLIDATED_MEMBER.equals(contextId)
                    && !XbrlConstants.CONTEXT_CURRENT_YEAR_DURATION_NON_CONSOLIDATED_MEMBER.equals(contextId)) {
                continue;
            }

            var periodNodeList = element.getElementsByTagName(XbrlConstants.XBRLI_PERIOD);
            if (periodNodeList.getLength() != 1) {
                throw new RuntimeException("Failed to parse XBRL content: period tag not found");
            }
            var periodNode = (Element) periodNodeList.item(0);
            // periodの子要素でどちらかは含まれるはず
            var instantNodeList = periodNode.getElementsByTagName(XbrlConstants.XBRLI_INSTANT);
            var endDateNodeList = periodNode.getElementsByTagName(XbrlConstants.XBRLI_END_DATE);
            String period = null;
            if (instantNodeList.getLength() == 1) {
                period = instantNodeList.item(0).getTextContent();
            }
            if (endDateNodeList.getLength() == 1) {
                period = endDateNodeList.item(0).getTextContent();
            }
            if (period == null) {
                throw new RuntimeException("Failed to parse XBRL content: period not found");
            }
            // YYYY-MM-DD形式
            return period;
        }
        throw new RuntimeException("Failed to parse XBRL content: context not found");
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
