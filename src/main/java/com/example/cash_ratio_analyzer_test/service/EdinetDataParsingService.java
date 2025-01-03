package com.example.cash_ratio_analyzer_test.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@Service
public class EdinetDataParsingService {

    @Value("${download.targetFilePrefix:}")
    private String targetFilePrefix;

    /**
     * 指定されたバイト配列（ZIP形式）からターゲットファイルを抽出します。
     *
     * @param fetchData 抽出対象のバイト配列
     * @return 抽出されたターゲットファイルの内容を表すバイト配列
     */
    public byte[] extractTargetFile(byte[] fetchData) {

        byte[] fileContent = null;
        try (
                var in = new ByteArrayInputStream(fetchData);
                var zipIn = new ZipInputStream(in)) {
            ZipEntry entry;

            while ((entry = zipIn.getNextEntry()) != null) {
                // TODO 調査：第５【経理の状況】を取得できる想定
                // EDINETからはXBRLとインスタンスXBRLが取得できるが、データ容量等を考慮してインスタンスXBRLを取得する
                if (entry.getName().startsWith(targetFilePrefix)) {
                    var out = new ByteArrayOutputStream();
                    var buffer = new byte[1024];
                    int len;
                    while ((len = zipIn.read(buffer)) != -1) {
                        out.write(buffer, 0, len);
                    }
                    fileContent = out.toByteArray();
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return fileContent;
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
