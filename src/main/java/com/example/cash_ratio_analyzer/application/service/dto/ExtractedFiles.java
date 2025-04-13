package com.example.cash_ratio_analyzer.application.service.dto;

import java.util.List;

public class ExtractedFiles {
    private final String headerOrFirstMainFileName;
    private final byte[] headerOrFirstMainContent;

    private final List<TargetFile> targetFiles;

    public ExtractedFiles(String headerFileName, byte[] headerContent, String firstMainFileName, byte[] firstMainContent, List<TargetFile> targetFiles) {
        if ((headerContent == null || headerContent.length == 0) && (firstMainContent == null || firstMainContent.length == 0)) {
            throw new IllegalArgumentException("Either headerContent or firstMainContent must be provided");
        }
        if (targetFiles == null || targetFiles.size() == 0) {
            throw new IllegalArgumentException("targetFile must be provided");
        }

        this.headerOrFirstMainFileName = (headerContent != null && headerContent.length > 0) ? headerFileName : firstMainFileName;
        this.headerOrFirstMainContent = (headerContent != null && headerContent.length > 0) ? headerContent : firstMainContent;
        this.targetFiles = targetFiles;
    }

    public String getHeaderOrFirstMainFileName() {
        return headerOrFirstMainFileName;
    }

    public byte[] getHeaderOrFirstMainContent() {
        return headerOrFirstMainContent;
    }

    public List<TargetFile> getTargetFiles() {
        return targetFiles;
    }

    public record TargetFile(String fileName, byte[] content) {
        public TargetFile {
            if (fileName == null || fileName.isEmpty()) {
                throw new IllegalArgumentException("Target file name must not be null or empty");
            }
            if (content == null || content.length == 0) {
                throw new IllegalArgumentException("Target file content must not be null or empty");
            }
        }
    }
}
