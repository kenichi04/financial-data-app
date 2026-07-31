package com.example.financialdataapp.application.service.metadata;

/**
 * 証券コードの表記ゆれを吸収します。
 * <p>
 * EDINETの {@code secCode} は末尾に0を付けた5桁（例: トヨタ自動車 {@code 72030}）で提供され、
 * {@code company.security_code} にはその形式のまま保存されている。
 * 一方で利用者・API仕様（docs/api_spec_draft.md）が扱うのは4桁の証券コードのため、
 * 「保存形式は変えず、入出力の境界で吸収する」方針をここに集約する。
 */
public final class SecurityCodeNormalizer {

    private static final int MARKET_CODE_LENGTH = 4;

    private SecurityCodeNormalizer() {
    }

    /**
     * 保存形式の証券コードを、利用者向けの4桁表記に変換します。
     * 5桁かつ末尾が0の場合のみ末尾を落とし、それ以外はそのまま返します。
     *
     * @param storedSecurityCode DBに保存されている証券コード
     * @return 4桁に正規化した証券コード（入力がnullの場合はnull）
     */
    public static String toMarketCode(String storedSecurityCode) {
        if (storedSecurityCode == null) {
            return null;
        }
        if (storedSecurityCode.length() == MARKET_CODE_LENGTH + 1 && storedSecurityCode.endsWith("0")) {
            return storedSecurityCode.substring(0, MARKET_CODE_LENGTH);
        }
        return storedSecurityCode;
    }
}
