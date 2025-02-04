package com.example.cash_ratio_analyzer_test.application.service.dto;

public class ExtractedFiles {
    private final String headerOrFirstMainFileName;
    private final byte[] headerOrFirstMainContent;

    private final String targetFileName;
    private final byte[] targetFileContent;

    public ExtractedFiles(String headerFileName, byte[] headerContent, String firstMainFileName, byte[] firstMainContent, String targetFileName, byte[] targetFileContent) {
        if ((headerContent == null || headerContent.length == 0) && (firstMainContent == null || firstMainContent.length == 0)) {
            throw new IllegalArgumentException("Either headerContent or firstMainContent must be provided");
        }
        if (targetFileContent == null || targetFileContent.length == 0) {
            throw new IllegalArgumentException("targetFileContent must be provided");
        }

        this.headerOrFirstMainFileName = (headerContent != null && headerContent.length > 0) ? headerFileName : firstMainFileName;
        this.headerOrFirstMainContent = (headerContent != null && headerContent.length > 0) ? headerContent : firstMainContent;
        this.targetFileName = targetFileName;
        this.targetFileContent = targetFileContent;
    }

    public String getHeaderOrFirstMainFileName() {
        return headerOrFirstMainFileName;
    }

    public byte[] getHeaderOrFirstMainContent() {
        return headerOrFirstMainContent;
    }

    public String getTargetFileName() {
        return targetFileName;
    }

    public byte[] getTargetFileContent() {
        return targetFileContent;
    }
}
