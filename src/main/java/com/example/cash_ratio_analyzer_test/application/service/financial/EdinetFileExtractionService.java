package com.example.cash_ratio_analyzer_test.application.service.financial;

import com.example.cash_ratio_analyzer_test.application.service.dto.ExtractedFiles;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
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

    private final String targetFilePrefix;

    public EdinetFileExtractionService(
            @Value("${download.targetFilePrefix:}")String targetFilePrefix) {
        this.targetFilePrefix = targetFilePrefix;
    }

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
        String targetFileName = null;
        byte[] targetFileContent = null;
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
                if (entry.getName().startsWith(INLINE_XBRL_FIRST_MAIN_FILE_PREFIX)) {
                    firstMainFileName = entry.getName();
                    firstMainContent = extractFileContent(zipIn);
                }
                // TODO 対象ファイルの選定
                // 一旦、「調査：第５【経理の状況】」のページを取得できる想定
                if (entry.getName().startsWith(targetFilePrefix)) {
                    targetFileName = entry.getName();
                    targetFileContent = extractFileContent(zipIn);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return new ExtractedFiles(headerFileName, headerContent, firstMainFileName, firstMainContent, targetFileName, targetFileContent);
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
}
