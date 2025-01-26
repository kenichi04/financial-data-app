package com.example.cash_ratio_analyzer_test.application.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
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

    @Value("${download.targetFilePrefix:}")
    private String targetFilePrefix;

    private static final String INLINE_XBRL_HEADER_FILE_PREFIX = "0000000_header";

    // TODO もう少し具体的なファイル名を指定する
    private static final String INLINE_XBRL_FIRST_MAIN_FILE_PREFIX = "0101";

    /**
     * 指定されたバイト配列（ZIP形式）からターゲットファイルを抽出します。
     *
     * @param fetchData 抽出対象のバイト配列
     * @return 抽出されたターゲットファイルの内容を表すバイト配列
     */
    public byte[] extractTargetFile(byte[] fetchData) {
        Map<String, byte[]> targetFileMap = new HashMap<>();

        byte[] headerContent = null;
        byte[] firstMainContent = null;
        byte[] targetFileContent = null;
        try (
                var in = new ByteArrayInputStream(fetchData);
                var zipIn = new ZipInputStream(in)) {
            ZipEntry entry;

            // EDINETからはXBRLとインスタンスXBRLが取得できるが、データ容量等を考慮してインスタンスXBRLを取得する
            while ((entry = zipIn.getNextEntry()) != null) {
                // 表紙ファイルにタグ情報あり（表紙ファイル無しの場合あり）
                if (entry.getName().startsWith(INLINE_XBRL_HEADER_FILE_PREFIX)) {
                    headerContent = extractFileContent(zipIn);
                }
                // 表紙ファイル無しの場合、本表の一つ目のファイルにタグ情報あり
                if (entry.getName().startsWith(INLINE_XBRL_FIRST_MAIN_FILE_PREFIX)) {
                    firstMainContent = extractFileContent(zipIn);
                }
                // 調査：第５【経理の状況】を取得できる想定
                // TODO 対象ファイルの選定
                if (entry.getName().startsWith(targetFilePrefix)) {
                    targetFileContent = extractFileContent(zipIn);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return targetFileContent;
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

    /**
     * 指定されたバイト配列（ZIP形式）からターゲットファイルを抽出し、UTF-8エンコーディングを使用して文字列に変換します。
     *
     * @param fetchData 抽出対象のバイト配列
     * @return 抽出されたターゲットファイルの内容を表す文字列
     * @throws RuntimeException UTF-8エンコーディングを使用してバイト配列を文字列に変換できなかった場合
     */
    public String extractTargetFileAsString(byte[] fetchData) {
        var targetFile = extractTargetFile(fetchData);
        try {
            return new String(targetFile, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(
                    "Failed to convert byte array to String using UTF-8 encoding: "
                            + e.getMessage());
        }
    }
}
