package com.example.cash_ratio_analyzer_test.application.service.dto;

public class ExtractedFiles {
    private final byte[] contentWithTagInfo;

    private final byte[] targetFileContent;

    public ExtractedFiles(byte[] headerContent, byte[] firstMainContent, byte[] targetFileContent) {
        if ((headerContent == null || headerContent.length == 0) && (firstMainContent == null || firstMainContent.length == 0)) {
            throw new IllegalArgumentException("Either headerContent or firstMainContent must be provided");
        }
        if (targetFileContent == null || targetFileContent.length == 0) {
            throw new IllegalArgumentException("targetFileContent must be provided");
        }
        this.contentWithTagInfo = (headerContent != null && headerContent.length > 0) ? headerContent : firstMainContent;
        this.targetFileContent = targetFileContent;
    }

    public byte[] getContentWithTagInfo() {
        return contentWithTagInfo;
    }

    public byte[] getTargetFileContent() {
        return targetFileContent;
    }
}
