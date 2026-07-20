package com.example.financialdataapp.application.service.metrics;

import com.example.financialdataapp.domain.enums.context.ConsolidatedType;
import com.example.financialdataapp.domain.enums.context.YearType;
import com.example.financialdataapp.domain.model.FinancialData;
import com.example.financialdataapp.domain.model.FinancialDocument;
import com.example.financialdataapp.domain.model.FinancialMetrics;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 財務書類から財務指標（ROE・ROA・営業利益率・自己資本比率・現金比率）を算出するサービスです。
 *
 * <p>算出ルール:</p>
 * <ul>
 *   <li>当期（CurrentYear）のデータのみを使用する</li>
 *   <li>連結データが存在する場合は連結を、無い場合は単体を使用する（混在させない）</li>
 *   <li>金額は display_scale で実額（円）に正規化してから計算する（科目間で表示単位が異なる場合があるため）</li>
 *   <li>売上高・自己資本は会計基準や業種により報告科目が異なるため、フォールバック順で解決する</li>
 *   <li>必要科目が無い・分母が0の指標はnull（他の指標は算出を継続する）</li>
 * </ul>
 */
@Service
public class FinancialMetricsCalculationService {

    /** %値の小数部桁数（DBのDECIMAL(10,4)と対応） */
    private static final int PERCENTAGE_SCALE = 4;

    private static final BigDecimal HUNDRED = BigDecimal.valueOf(100);

    /** 当期純利益 */
    private static final List<String> NET_INCOME_CODES = List.of("ProfitLoss");

    /** 総資産 */
    private static final List<String> TOTAL_ASSETS_CODES = List.of("Assets");

    /** 現金及び預金 */
    private static final List<String> CASH_CODES = List.of("CashAndDeposits");

    /** 営業利益 */
    private static final List<String> OPERATING_INCOME_CODES = List.of("OperatingIncome");

    /** 純資産（自己資本比率の分子） */
    private static final List<String> NET_ASSETS_CODES = List.of("NetAssets");

    /** ROE分母の自己資本。株主資本を優先し、無ければ純資産で代用する */
    private static final List<String> EQUITY_CODES = List.of("ShareholdersEquity", "NetAssets");

    /** 売上高。日本基準・IFRS・業種別の報告科目のフォールバック順 */
    private static final List<String> NET_SALES_CODES =
            List.of("NetSales", "Revenue", "OperatingRevenue1", "OperatingRevenue2");

    /**
     * 財務書類から財務指標を算出します。
     *
     * @param document 算出元の財務書類
     * @param accountCodeById 勘定科目ID→勘定科目コード（account_master.code）のマップ
     * @return 算出された財務指標（算出不能な指標はnullを保持）
     */
    public FinancialMetrics calculate(FinancialDocument document, Map<Long, String> accountCodeById) {
        var consolidatedType = determineConsolidatedType(document.getData());
        var amountByCode = collectCurrentYearAmounts(document.getData(), accountCodeById, consolidatedType);

        var netIncome = findAmount(amountByCode, NET_INCOME_CODES);
        var totalAssets = findAmount(amountByCode, TOTAL_ASSETS_CODES);
        var equity = findAmount(amountByCode, EQUITY_CODES);
        var netAssets = findAmount(amountByCode, NET_ASSETS_CODES);
        var operatingIncome = findAmount(amountByCode, OPERATING_INCOME_CODES);
        var netSales = findAmount(amountByCode, NET_SALES_CODES);
        var cash = findAmount(amountByCode, CASH_CODES);

        return new FinancialMetrics(
                document.getDocumentId(),
                document.getEdinetCode(),
                document.getFiscalYearEndDate(),
                consolidatedType,
                percentage(netIncome, equity),
                percentage(netIncome, totalAssets),
                percentage(operatingIncome, netSales),
                percentage(netAssets, totalAssets),
                percentage(cash, totalAssets));
    }

    /**
     * 使用する連結区分を決定します。当期の連結データが1件でもあれば連結を優先します。
     */
    private ConsolidatedType determineConsolidatedType(List<FinancialData> dataList) {
        var hasConsolidated = dataList.stream()
                .filter(data -> data.getEdinetContext().getPeriodType() == YearType.CURRENT_YEAR)
                .anyMatch(data -> data.getEdinetContext().getConsolidatedType() == ConsolidatedType.CONSOLIDATED);
        return hasConsolidated ? ConsolidatedType.CONSOLIDATED : ConsolidatedType.NON_CONSOLIDATED;
    }

    /**
     * 当期かつ指定の連結区分のデータを「勘定科目コード→実額（円）」のマップに集約します。
     */
    private Map<String, BigDecimal> collectCurrentYearAmounts(
            List<FinancialData> dataList, Map<Long, String> accountCodeById, ConsolidatedType consolidatedType) {
        var amountByCode = new HashMap<String, BigDecimal>();
        for (var data : dataList) {
            var context = data.getEdinetContext();
            if (context.getPeriodType() != YearType.CURRENT_YEAR
                    || context.getConsolidatedType() != consolidatedType) {
                continue;
            }
            var code = accountCodeById.get(data.getAccountId());
            if (code == null) {
                continue;
            }
            amountByCode.putIfAbsent(code, normalizeAmount(data));
        }
        return amountByCode;
    }

    /**
     * 保存されている金額（表示単位の値）をdisplay_scaleで実額（円）に正規化します。
     * 例: 金額1,234・百万円単位（scale=6）→ 1,234,000,000円
     */
    private BigDecimal normalizeAmount(FinancialData data) {
        return data.getAmount().scaleByPowerOfTen(data.getDisplayScale().getCode());
    }

    /**
     * フォールバック順で最初に見つかった勘定科目の金額を返します。無ければnullです。
     */
    private BigDecimal findAmount(Map<String, BigDecimal> amountByCode, List<String> codes) {
        for (var code : codes) {
            var amount = amountByCode.get(code);
            if (amount != null) {
                return amount;
            }
        }
        return null;
    }

    /**
     * 分子 ÷ 分母 × 100 を小数第4位（四捨五入）で返します。算出不能な場合はnullです。
     */
    private BigDecimal percentage(BigDecimal numerator, BigDecimal denominator) {
        if (numerator == null || denominator == null || denominator.signum() == 0) {
            return null;
        }
        return numerator.multiply(HUNDRED).divide(denominator, PERCENTAGE_SCALE, RoundingMode.HALF_UP);
    }
}
