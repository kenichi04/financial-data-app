package com.example.cash_ratio_analyzer_test.domain.service;

import com.example.cash_ratio_analyzer_test.domain.enums.EdinetDocumentType;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;

// ドメインサービスとして定義
@Service
public class DocumentService {

    /**
     * 指定された書類コードが許可された書類タイプかどうかを判定します。
     *
     * @param documentTypeCode 書類タイプのコード
     * @return 許可された書類タイプの場合はtrue、それ以外の場合はfalse
     */
    public boolean isPermittedDocumentType(String documentTypeCode) {
        if (StringUtils.isEmpty(documentTypeCode)) {
            return false;
        }

        var code = Integer.parseInt(documentTypeCode);
        // 一旦、有価証券報告書のみ対象とする
        // TODO 他の書類も対象にする場合は、リストに追加する
        var permittedTypeCodes = List.of(
                EdinetDocumentType.YUKASHOKEN_HOKOKUSHO.code()
        );
        return permittedTypeCodes.contains(code);
    }
}
