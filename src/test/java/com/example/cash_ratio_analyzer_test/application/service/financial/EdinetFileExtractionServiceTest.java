package com.example.cash_ratio_analyzer_test.application.service.financial;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static org.junit.jupiter.api.Assertions.*;

class EdinetFileExtractionServiceTest {

    private EdinetFileExtractionService edinetFileExtractionService;

    private static final String targetFilePrefix = "XBRL/PublicDoc/0105020";

    @BeforeEach
    void setUp() {
        edinetFileExtractionService = new EdinetFileExtractionService(targetFilePrefix);
    }

    @Test
    void extractTargetFile() throws IOException {
        // given
        byte[] fetchData = createZipData();

        // when
        var extractedFiles = edinetFileExtractionService.extractTargetFile(fetchData);

        // then
        assertNotNull(extractedFiles.getHeaderOrFirstMainContent());
        assertNotNull(extractedFiles.getTargetFileContent());
        assertEquals("XBRL/PublicDoc/0000000_header.xml", extractedFiles.getHeaderOrFirstMainFileName());
        assertEquals("XBRL/PublicDoc/0105020TEST.xml", extractedFiles.getTargetFileName());
    }

    private byte[] createZipData() throws IOException {
        var out = new ByteArrayOutputStream();
        try (var zipOut = new ZipOutputStream(out)) {
            addZipEntry(zipOut, "XBRL/PublicDoc/0000000_header.xml", "header-content");
            addZipEntry(zipOut, "XBRL/PublicDoc/0101TEST.xml", "first-content");
            addZipEntry(zipOut, "XBRL/PublicDoc/0105020TEST.xml", "target-content");
        }
        return out.toByteArray();
    }

    private void addZipEntry(ZipOutputStream zipOut, String fileName, String content) throws IOException {
        zipOut.putNextEntry(new ZipEntry(fileName));
        zipOut.write(content.getBytes());
        zipOut.closeEntry();
    }
}