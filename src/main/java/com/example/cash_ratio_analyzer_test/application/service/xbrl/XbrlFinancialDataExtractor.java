package com.example.cash_ratio_analyzer_test.application.service.xbrl;

import com.example.cash_ratio_analyzer_test.application.service.AccountService;
import com.example.cash_ratio_analyzer_test.application.service.constants.XbrlConstants;
import com.example.cash_ratio_analyzer_test.domain.enums.DisplayScale;
import com.example.cash_ratio_analyzer_test.domain.enums.Currency;
import com.example.cash_ratio_analyzer_test.domain.model.Account;
import com.example.cash_ratio_analyzer_test.domain.model.FinancialData;
import org.springframework.stereotype.Service;
import org.w3c.dom.Element;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

// TODO インターフェース作成して、切替可能にする
/**
 * XBRLコンテンツから財務データを抽出するサービスクラス。
 * このクラスは、XBRLコンテンツを解析し、関連する財務データをFinancialDataオブジェクトのリストに抽出するメソッドを提供します。
 */
@Service
public class XbrlFinancialDataExtractor {

    private final AccountService accountService;

    private final XbrlDocumentParser xbrlDocumentParser;

    public XbrlFinancialDataExtractor(AccountService accountService, XbrlDocumentParser xbrlDocumentParser) {
        this.accountService = accountService;
        this.xbrlDocumentParser = xbrlDocumentParser;
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

        var document = xbrlDocumentParser.parseDocumentToDom(xbrlContent);
        var elements = document.getDocumentElement();
        // 金額を取得するため、ix:nonFraction要素を取得
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
        var scale = element.getAttribute(XbrlConstants.ATTRIBUTE_SCALE);
        var sign = element.getAttribute(XbrlConstants.ATTRIBUTE_SIGN);
        var value = extractValueFromElement(element, XbrlConstants.MINUS.equals(sign));
        // 金額が0の場合は登録しない
        if (value.equals(BigDecimal.ZERO)) {
            return Optional.empty();
        }

        var account = accountMap.get(name);
        return Optional.of(
                new FinancialData(account, contextRef, value,
                        DisplayScale.fromCode(Integer.parseInt(scale)),
                        Currency.fromCode(unitRef)));
    }

    /**
     * 指定されたXML要素から数値を抽出します。
     *
     * @param element XML要素
     * @return 抽出された数値、数値が無い場合はBigDecimal.ZERO
     */
    private BigDecimal extractValueFromElement(Element element, boolean isMinus) {
        // 金額はカンマ区切り
        var valueText = element.getTextContent().trim().replace(XbrlConstants.COMMA, "");
        var value = isMinus ? XbrlConstants.MINUS + valueText : valueText;
        try {
            return new BigDecimal(value);
        } catch (NumberFormatException e) {
            return BigDecimal.ZERO;
        }
    }
}
