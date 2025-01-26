package com.example.cash_ratio_analyzer_test.application.service.dto;

public class ExtractedFiles {
    private final byte[] headerContent;
    private final byte[] firstMainContent;
    private final byte[] targetFileContent;

    public ExtractedFiles(byte[] headerContent, byte[] firstMainContent, byte[] targetFileContent) {
        this.headerContent = headerContent;
        this.firstMainContent = firstMainContent;
        this.targetFileContent = targetFileContent;
    }

    public byte[] getHeaderContent() {
        return headerContent;
    }

    public byte[] getFirstMainContent() {
        return firstMainContent;
    }

    public byte[] getTargetFileContent() {
        return targetFileContent;
    }
}
