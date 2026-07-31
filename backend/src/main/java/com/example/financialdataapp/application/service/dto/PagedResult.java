package com.example.financialdataapp.application.service.dto;

import java.util.List;

/**
 * ページングされたクエリ結果です。プレゼンテーション層の表現に依存しないよう、
 * 総ページ数などの導出値は持たせずクエリ結果のみを保持します。
 *
 * @param content       このページの要素
 * @param totalElements 全件数
 * @param <T>           要素の型
 */
public record PagedResult<T>(List<T> content, long totalElements) {
}
