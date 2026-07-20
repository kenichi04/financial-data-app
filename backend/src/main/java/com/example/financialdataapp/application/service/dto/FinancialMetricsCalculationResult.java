package com.example.financialdataapp.application.service.dto;

import java.util.List;

/**
 * 財務指標バッチ計算の実行結果です。
 *
 * @param targetCount 計算対象（未計算）の書類数
 * @param savedCount 計算・保存に成功した書類数
 * @param failedDocumentIds 計算・保存に失敗した書類IDのリスト
 */
public record FinancialMetricsCalculationResult(
        int targetCount,
        int savedCount,
        List<String> failedDocumentIds) {
}
