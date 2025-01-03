package com.example.cash_ratio_analyzer_test.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@Service
public class EdinetDataParsingService {

    @Value("${download.targetFilePrefix:}")
    private String targetFilePrefix;

    public byte[] extractTargetFile(byte[] fetchData) {

        byte[] fileContent = null;
        try (
                var in = new ByteArrayInputStream(fetchData);
                var zipIn = new ZipInputStream(in)) {
            ZipEntry entry;

            while ((entry = zipIn.getNextEntry()) != null) {
//                var file = new File(entry.getName());
//                if (entry.isDirectory()) {
//                    file.mkdirs();
//                } else {
//                    // TODO ファイルの解析処理を記述
//                }
                // TODO 調査：第５【経理の状況】を取得できる想定
                if (entry.getName().startsWith(targetFilePrefix)) {
                    // TODO readAllBytes()は使用しない
                    // https://docs.oracle.com/javase/jp/9/docs/api/java/io/InputStream.html#readAllBytes--
                    fileContent = zipIn.readAllBytes();
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return fileContent;
    }
}
