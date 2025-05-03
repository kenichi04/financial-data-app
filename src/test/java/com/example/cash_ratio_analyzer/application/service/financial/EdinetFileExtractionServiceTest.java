package com.example.cash_ratio_analyzer.application.service.financial;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static org.junit.jupiter.api.Assertions.*;

class EdinetFileExtractionServiceTest {

    private static final String TEST_XBRL_HEADER_FILE = "XBRL/PublicDoc/0000000_header.xml";

    private static final String TEST_XBRL_FIRST_MAIN_FILE = "XBRL/PublicDoc/0101_TEST.xml";

    private static final String TEST_XBRL_TARGET_FILE = "XBRL/PublicDoc/0105020_TEST-TARGET.xml";

    private EdinetFileExtractionService edinetFileExtractionService;

    @BeforeEach
    void setUp() {
        edinetFileExtractionService = new EdinetFileExtractionService();
    }

    @Test
    void extractTargetFile_shouldExtractCorrectFiles() throws IOException {
        // given
        byte[] fetchData = createZipData();

        // when
        var extractedFiles = edinetFileExtractionService.extractTargetFile(fetchData);

        // then
        var firstTargetFile = extractedFiles.getTargetFiles().get(0);
        assertNotNull(extractedFiles.getHeaderOrFirstMainContent());
        assertNotNull(firstTargetFile);

        assertEquals(TEST_XBRL_HEADER_FILE, extractedFiles.getHeaderOrFirstMainFileName());
        assertEquals(TEST_XBRL_TARGET_FILE, firstTargetFile.fileName());

        assertArrayEquals("header-content".getBytes(), extractedFiles.getHeaderOrFirstMainContent());
        assertArrayEquals("target-content".getBytes(), firstTargetFile.content());
    }

    @Test
    void extractTargetFile_shouldExtractFirstMainFile_whenHeaderFileIsMissing() throws IOException {
        // given
        byte[] fetchData = createZipDataWithoutHeaderFile();

        // when
        var extractedFiles = edinetFileExtractionService.extractTargetFile(fetchData);

        // then
        var firstTargetFile = extractedFiles.getTargetFiles().get(0);
        assertNotNull(extractedFiles.getHeaderOrFirstMainContent());
        assertNotNull(firstTargetFile.content());

        assertEquals(TEST_XBRL_FIRST_MAIN_FILE, extractedFiles.getHeaderOrFirstMainFileName());
        assertEquals(TEST_XBRL_TARGET_FILE, firstTargetFile.fileName());

        assertArrayEquals("first-content".getBytes(), extractedFiles.getHeaderOrFirstMainContent());
        assertArrayEquals("target-content".getBytes(), firstTargetFile.content());
    }

    @Test
    void extractTargetFile_shouldThrowException_whenTargetFileIsMissing() throws IOException {
        byte[] fetchData = createZipDataWithoutTargetFile();

        assertThrows(IllegalArgumentException.class,
                () -> edinetFileExtractionService.extractTargetFile(fetchData));
    }

    @Test
    void extractTargetFile_shouldThrowException_whenHeaderAndFirstMainFilesAreMissing() throws IOException {
        byte[] fetchData = createZipDataWithoutHeaderAndFirstMainFiles();

        assertThrows(IllegalArgumentException.class,
                () -> edinetFileExtractionService.extractTargetFile(fetchData));
    }

    private byte[] createZipData() throws IOException {
        var out = new ByteArrayOutputStream();
        try (var zipOut = new ZipOutputStream(out)) {
            addZipEntry(zipOut, TEST_XBRL_HEADER_FILE, "header-content");
            addZipEntry(zipOut, TEST_XBRL_FIRST_MAIN_FILE, "first-content");
            addZipEntry(zipOut, TEST_XBRL_TARGET_FILE, "target-content");
        }
        return out.toByteArray();
    }

    private byte[] createZipDataWithoutHeaderFile() throws IOException {
        var out = new ByteArrayOutputStream();
        try (var zipOut = new ZipOutputStream(out)) {
            // header file is missing
            addZipEntry(zipOut, TEST_XBRL_FIRST_MAIN_FILE, "first-content");
            addZipEntry(zipOut, TEST_XBRL_TARGET_FILE, "target-content");
        }
        return out.toByteArray();
    }

    private byte[] createZipDataWithoutTargetFile() throws IOException {
        var out = new ByteArrayOutputStream();
        try (var zipOut = new ZipOutputStream(out)) {
            addZipEntry(zipOut, TEST_XBRL_HEADER_FILE, "header-content");
            addZipEntry(zipOut, TEST_XBRL_FIRST_MAIN_FILE, "first-content");
            // target file is missing
        }
        return out.toByteArray();
    }

    private byte[] createZipDataWithoutHeaderAndFirstMainFiles() throws IOException {
        var out = new ByteArrayOutputStream();
        try (var zipOut = new ZipOutputStream(out)) {
            // header and first main files are missing
            addZipEntry(zipOut, TEST_XBRL_TARGET_FILE, "target-content");
        }
        return out.toByteArray();
    }

    private void addZipEntry(ZipOutputStream zipOut, String fileName, String content) throws IOException {
        zipOut.putNextEntry(new ZipEntry(fileName));
        zipOut.write(content.getBytes());
        zipOut.closeEntry();
    }
    
    /**
     * 連結データと単体データの特定に使用するサンプルコンテンツを作成
     */
    private String createConsolidatedContent() {
        return "<ix:nonNumeric name=\"jpcrp_cor:ConsolidatedBalanceSheetTextBlock\" contextRef=\"CurrentYearDuration\" escape=\"true\">";
    }

    private String createNonConsolidatedContent() {
        return "<ix:nonNumeric name=\"jpcrp_cor:BalanceSheetTextBlock\" contextRef=\"CurrentYearDuration_NonConsolidatedMember\" escape=\"true\">";
    }
    
    /**
     * 連結と単体の両方を含むZIPデータを作成
     */
    private byte[] createZipDataWithBothConsolidatedAndNonConsolidated() throws IOException {
        var out = new ByteArrayOutputStream();
        try (var zipOut = new ZipOutputStream(out)) {
            addZipEntry(zipOut, TEST_XBRL_HEADER_FILE, "header-content");
            addZipEntry(zipOut, TEST_XBRL_FIRST_MAIN_FILE, "first-content");
            addZipEntry(zipOut, "XBRL/PublicDoc/0105020_CONSOLIDATED.xml", createConsolidatedContent());
            addZipEntry(zipOut, "XBRL/PublicDoc/0105020_NONCONSOLIDATED.xml", createNonConsolidatedContent());
        }
        return out.toByteArray();
    }
    
    /**
     * 単体データのみを含むZIPデータを作成
     */
    private byte[] createZipDataWithOnlyNonConsolidated() throws IOException {
        var out = new ByteArrayOutputStream();
        try (var zipOut = new ZipOutputStream(out)) {
            addZipEntry(zipOut, TEST_XBRL_HEADER_FILE, "header-content");
            addZipEntry(zipOut, TEST_XBRL_FIRST_MAIN_FILE, "first-content");
            addZipEntry(zipOut, "XBRL/PublicDoc/0105020_NONCONSOLIDATED.xml", createNonConsolidatedContent());
        }
        return out.toByteArray();
    }
    
    @Test
    void extractTargetFile_shouldExtractOnlyConsolidated_whenBothAreAvailable() throws IOException {
        // given
        byte[] fetchData = createZipDataWithBothConsolidatedAndNonConsolidated();

        // when
        var extractedFiles = edinetFileExtractionService.extractTargetFile(fetchData);

        // then
        assertEquals(1, extractedFiles.getTargetFiles().size());
        assertTrue(extractedFiles.isConsolidated());
        assertEquals("XBRL/PublicDoc/0105020_CONSOLIDATED.xml", extractedFiles.getTargetFiles().get(0).fileName());
    }

    @Test
    void extractTargetFile_shouldExtractNonConsolidated_whenOnlyNonConsolidatedIsAvailable() throws IOException {
        // given
        byte[] fetchData = createZipDataWithOnlyNonConsolidated();

        // when
        var extractedFiles = edinetFileExtractionService.extractTargetFile(fetchData);

        // then
        assertEquals(1, extractedFiles.getTargetFiles().size());
        assertFalse(extractedFiles.isConsolidated());
        assertEquals("XBRL/PublicDoc/0105020_NONCONSOLIDATED.xml", extractedFiles.getTargetFiles().get(0).fileName());
    }

    @Test
    void extractTargetFile_shouldCorrectlyIdentifyConsolidatedData() throws IOException {
        // given
        byte[] fetchData = createZipDataWithBothConsolidatedAndNonConsolidated();

        // when
        var extractedFiles = edinetFileExtractionService.extractTargetFile(fetchData);

        // then
        assertTrue(extractedFiles.isConsolidated());
    }
}
