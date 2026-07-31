package com.example.financialdataapp.presentation.controller.response;

import java.util.List;

/**
 * 一覧・検索系エンドポイント共通のページレスポンスです。
 * 構造は docs/api_spec_draft.md「ページネーション」に準拠します。
 *
 * @param content       このページの要素
 * @param totalElements 全件数
 * @param totalPages    全ページ数（全件数0のときは0）
 * @param page          ページ番号（0始まり）
 * @param size          1ページあたりの件数
 * @param <T>           要素の型
 */
public record PageResponse<T>(
        List<T> content,
        long totalElements,
        int totalPages,
        int page,
        int size
) {

    /**
     * 全件数とページ指定から総ページ数を算出してレスポンスを生成します。
     *
     * @param content       このページの要素
     * @param totalElements 全件数
     * @param page          ページ番号（0始まり）
     * @param size          1ページあたりの件数（1以上）
     * @param <T>           要素の型
     * @return ページレスポンス
     */
    public static <T> PageResponse<T> of(List<T> content, long totalElements, int page, int size) {
        if (size < 1) {
            throw new IllegalArgumentException("size must be 1 or greater: " + size);
        }
        int totalPages = (int) ((totalElements + size - 1) / size);
        return new PageResponse<>(content, totalElements, totalPages, page, size);
    }
}
