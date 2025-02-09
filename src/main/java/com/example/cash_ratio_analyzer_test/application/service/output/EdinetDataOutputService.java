package com.example.cash_ratio_analyzer_test.application.service.output;

import com.example.cash_ratio_analyzer_test.application.service.enums.FetchDocumentType;
import com.example.cash_ratio_analyzer_test.application.service.financial.EdinetDataFetchService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.nio.file.Path;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@Service
public class EdinetDataOutputService {

    private final EdinetDataFetchService edinetDataFetchService;

    @Value("${download.userDir:}")
    private String userDir;

    @Value("${download.targetFilePrefix:}")
    private String targetFilePrefix;

    public EdinetDataOutputService(EdinetDataFetchService edinetDataFetchService) {
        this.edinetDataFetchService = edinetDataFetchService;
    }

    RestTemplate restTemplate = new RestTemplate();

    public String testFetchEdinetZipData(FetchDocumentType type, String documentNumber) {
        var fetchData = edinetDataFetchService.fetchFinancialData(type, documentNumber);

        var extension = ".zip";
        var fileName = String.format(
                "%s_%s%s",documentNumber, type.name(), extension);
        outputFile(fetchData, fileName);

        return "zip file is saved in your download dir.";
    }

    public String testFetchEdinetPdfData(String documentNumber) {
        var fetchData = edinetDataFetchService.fetchFinancialData(FetchDocumentType.PDF, documentNumber);

        var extension = ".pdf";
        outputFile(fetchData, documentNumber + extension);

        return "pdf file is saved in your download dir.";
    }

    public String testFetchEdinetXbrlData(String documentNumber) {
        // zip形式のバイナリデータ
        var zipData = edinetDataFetchService.fetchFinancialData(FetchDocumentType.XBRL, documentNumber);

        byte[] fileContent = null;
        try (
                var in = new ByteArrayInputStream(zipData);
                var zipIn = new ZipInputStream(in)) {
            ZipEntry entry;
            while ((entry = zipIn.getNextEntry()) != null) {
                // TODO 調査：第５【経理の状況】を取得できる想定
                if (entry.getName().startsWith(targetFilePrefix)) {
                    fileContent = zipIn.readAllBytes();
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if (fileContent != null) {
            // TODO resultディレクトリ用意しないとエラーになる
            outputFile(fileContent, "result/" + "test_ixbrl.htm");
            return "file is saved in your download dir.";
        } else {
            return "file not found.";
        }
    }

    private void outputFile(byte[] body, String fileName) {
        final var location = String.format(
                "C:/Users/%s/Downloads/%s", userDir, fileName);
        final var path = Path.of(location);

        try (
                var out = new BufferedOutputStream(
                        new FileOutputStream(path.toFile()))) {
            out.write(body);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
