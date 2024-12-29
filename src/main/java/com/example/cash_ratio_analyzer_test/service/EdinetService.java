package com.example.cash_ratio_analyzer_test.service;

import com.example.cash_ratio_analyzer_test.EdinetType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.nio.file.Path;

@Service
public class EdinetService {

    // 書類取得API
    private static final String EDINET_FETCH_URL
            = "https://api.edinet-fsa.go.jp/api/v2/documents/{docNumber}?type={type}&Subscription-Key={key}";

    @Value("${edinet.api.subscriptionKey:}")
    private String subscriptionKey;

    @Value("${download.userDir:}")
    private String userDir;

    RestTemplate restTemplate = new RestTemplate();

    public String testFetchEdinetXbrlData(String docNumber) {

        ResponseEntity<byte[]> response = restTemplate.exchange(
                EDINET_FETCH_URL, HttpMethod.GET, null,
                byte[].class, docNumber, EdinetType.XBRL.code(), subscriptionKey);

        var extension = ".zip";
        saveFileToDownloadDir(response.getBody(), docNumber + extension);

        return "zip file is saved in your download dir.";
    }

    public String testFetchEdinetPdfData(String docNumber) {
        var response = restTemplate.exchange(
                EDINET_FETCH_URL, HttpMethod.GET, null,
                byte[].class, docNumber, EdinetType.PDF.code(), subscriptionKey);

        var extension = ".pdf";
        saveFileToDownloadDir(response.getBody(), docNumber + extension);

        return "pdf file is saved in your download dir.";
    }

    private void saveFileToDownloadDir(byte[] body, String fileName) {
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
