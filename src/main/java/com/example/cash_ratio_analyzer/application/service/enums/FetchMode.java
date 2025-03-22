package com.example.cash_ratio_analyzer.application.service.enums;

/**
 * EDINET書類一覧APIのパラメータで使用する取得モードを表す列挙型.
 */
public enum FetchMode {
    /** メタデータのみ, 指定ない場合のデフォルト */
    METADATA_ONLY(1),
    /** メタデータおよび書類一覧 */
    METADATA_AND_LIST(2);

    private final int id;

    private FetchMode(int id) {
        this.id = id;
    }

    public int code() {
        return this.id;
    }
}
