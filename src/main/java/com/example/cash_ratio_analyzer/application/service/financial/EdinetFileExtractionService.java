package com.example.cash_ratio_analyzer.application.service.financial;

import com.example.cash_ratio_analyzer.application.service.constants.XbrlConstants;
import com.example.cash_ratio_analyzer.application.service.dto.ExtractedFiles;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * EDINETファイル抽出サービスクラス。
 * 指定されたバイト配列（ZIP形式）からターゲットファイルを抽出する機能を提供します。
 */
@Service
public class EdinetFileExtractionService {

    // TODO 拡張子チェックも追加する？
    private static final String INLINE_XBRL_HEADER_FILE_PREFIX = "XBRL/PublicDoc/0000000_header";

    // TODO もう少し具体的なファイル名を指定する
    private static final String INLINE_XBRL_FIRST_MAIN_FILE_PREFIX = "XBRL/PublicDoc/0101";

    // 有価証券報告書（3号様式）> 第一部【企業情報】第５【経理の状況】のファイル（複数）
    private static final String INLINE_XBRL_TARGET_FILES_PREFIX = "XBRL/PublicDoc/0105";

    /**
     * 指定されたバイト配列（ZIP形式）からターゲットファイルを抽出します。
     *
     * @param fetchData 抽出対象のバイト配列
     * @return 抽出されたターゲットファイルの内容を表すバイト配列を持つコンテナクラス
     */
    public ExtractedFiles extractTargetFile(byte[] fetchData) {
        Map<String, byte[]> targetFileMap = new HashMap<>();

        String headerFileName = null;
        byte[] headerContent = null;
        String firstMainFileName = null;
        byte[] firstMainContent = null;
        List<ExtractedFiles.TargetFile> targetFiles = new ArrayList<>();
        try (
                var in = new ByteArrayInputStream(fetchData);
                var zipIn = new ZipInputStream(in)) {
            ZipEntry entry;

            // EDINETからはXBRLインスタンスとインラインXBRLが取得できるが、データ容量等を考慮してインラインXBRLを取得する
            while ((entry = zipIn.getNextEntry()) != null) {
                // 表紙ファイルにタグ情報あり（表紙ファイル無しの場合あり）
                if (entry.getName().startsWith(INLINE_XBRL_HEADER_FILE_PREFIX)) {
                    headerFileName = entry.getName();
                    headerContent = extractFileContent(zipIn);
                }
                // 表紙ファイル無しの場合、本表の一つ目のファイルにタグ情報あり
                // ただし有価証券報告書の場合は表紙ファイルあるはずなので、一旦考慮不要かも
                if (entry.getName().startsWith(INLINE_XBRL_FIRST_MAIN_FILE_PREFIX)) {
                    // TODO firstMainFileName != nullの場合だけでよさそう
                    firstMainFileName = entry.getName();
                    firstMainContent = extractFileContent(zipIn);
                }
                if (entry.getName().startsWith(INLINE_XBRL_TARGET_FILES_PREFIX)) {
                    var targetFileName = entry.getName();
                    var targetFileContent = extractFileContent(zipIn);

                    // 簡易フィルタ. 必要なタグが含まれるファイルか判定
                    if (!isRelevantTargetFile(targetFileContent)) {
                        continue;
                    }
                    // TODO 単体の決算内容と連結の決算内容の両方を含むケースの検討
                    targetFiles.add(new ExtractedFiles.TargetFile(targetFileName, targetFileContent));
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to extract target file", e);
        }

        return new ExtractedFiles(headerFileName, headerContent, firstMainFileName, firstMainContent, targetFiles);
    }

    /**
     * ZIP入力ストリームからファイルの内容を抽出します。
     *
     * @param zipIn ZIP入力ストリーム
     * @return 抽出されたファイルの内容を表すバイト配列
     * @throws IOException 入出力エラーが発生した場合
     */
    private byte[] extractFileContent(ZipInputStream zipIn) throws IOException {
        var out = new ByteArrayOutputStream();
        var buffer = new byte[1024];
        int len;
        while ((len = zipIn.read(buffer)) != -1) {
            out.write(buffer, 0, len);
        }
        return out.toByteArray();
    }

    private boolean isRelevantTargetFile(byte[] content) {
        var xmlData = new String(content, StandardCharsets.UTF_8);
        // TODO チェック方法は要確認
        // 1つのファイルに隅付き括弧【】が必ず含まれるらしいので、それで判定しても良いか？
        return xmlData.contains(XbrlConstants.JP_CPR_COR_NAMESPACE + XbrlConstants.CONSOLIDATED_BS_TEXT_BLOCK) ||
                xmlData.contains(XbrlConstants.JP_CPR_COR_NAMESPACE + XbrlConstants.CONSOLIDATED_PL_TEXT_BLOCK) ||
                xmlData.contains(XbrlConstants.JP_CPR_COR_NAMESPACE + XbrlConstants.BS_TEXT_BLOCK) ||
                xmlData.contains(XbrlConstants.JP_CPR_COR_NAMESPACE + XbrlConstants.PL_TEXT_BLOCK);
    }
}
