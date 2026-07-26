package com.example.financialdataapp.application.service.financial;

import com.example.financialdataapp.domain.model.FinancialData;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 1書類分の財務データから重複を排除するサービス。
 *
 * <p>iXBRL（インラインXBRL）では同一の事実（同じ勘定科目×同じコンテキスト）が本表・注記など
 * 複数のセクションファイルに繰り返し出現するため、抽出結果をそのまま保存すると
 * {@code (financial_document_id, account_id, context_ref)} 単位で行が重複する。
 * ここで {@code (accountId, contextRef)} をキーに先勝ちで一意化する。
 *
 * <p>同一キーの重複は通常まったく同じ値だが、念のため「正規化した実額（amount × 10^displayScale）と通貨」で
 * 同一性を検証し、食い違う場合はどれが正しいか判断できない異常として例外を投げる（fail-fast）。
 * 呼び出し側（バッチ等）は書類単位で捕捉し、その書類のみスキップする想定。
 */
@Service
public class FinancialDataDeduplicator {

    /**
     * 財務データリストを {@code (accountId, contextRef)} 単位で重複排除する。
     *
     * @param documentId ログ・例外メッセージ用の書類ID
     * @param dataList   抽出直後の財務データ（重複を含みうる）
     * @return 重複を除いた財務データ（元の出現順を保持）
     * @throws IllegalStateException 同一キーで実額または通貨が食い違う場合
     */
    public List<FinancialData> deduplicate(String documentId, List<FinancialData> dataList) {
        Map<Key, FinancialData> byKey = new LinkedHashMap<>();

        for (FinancialData data : dataList) {
            var key = new Key(data.getAccountId(), data.getEdinetContext().getContextRef());
            var existing = byKey.get(key);
            if (existing == null) {
                byKey.put(key, data);
                continue;
            }
            if (!isSameEffectiveValue(existing, data)) {
                throw new IllegalStateException(String.format(
                        "Conflicting financial data in document %s: accountId=%d contextRef=%s -> %s vs %s",
                        documentId, key.accountId(), key.contextRef(),
                        describe(existing), describe(data)));
            }
            // 実額・通貨が一致する真の重複は先勝ちで捨てる（情報欠落なし）
        }

        return new ArrayList<>(byKey.values());
    }

    /**
     * 表示単位（displayScale）と通貨を考慮した実額が等しいかを判定する。
     * 例: (amount=6470933, scale=3) と (amount=6470.933, scale=6) は同一実額とみなす。
     */
    private boolean isSameEffectiveValue(FinancialData a, FinancialData b) {
        if (a.getCurrency() != b.getCurrency()) {
            return false;
        }
        return effectiveAmount(a).compareTo(effectiveAmount(b)) == 0;
    }

    private BigDecimal effectiveAmount(FinancialData data) {
        return data.getAmount().movePointRight(data.getDisplayScale().getCode());
    }

    private String describe(FinancialData data) {
        return String.format("amount=%s scale=%d currency=%s",
                data.getAmount(), data.getDisplayScale().getCode(), data.getCurrency());
    }

    private record Key(Long accountId, String contextRef) {}
}
