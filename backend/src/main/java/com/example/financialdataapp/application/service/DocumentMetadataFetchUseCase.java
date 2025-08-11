package com.example.financialdataapp.application.service;

import com.example.financialdataapp.application.service.enums.FetchMode;
import com.example.financialdataapp.application.service.metadata.EdinetDocumentListService;
import com.example.financialdataapp.application.service.metadata.DocumentMetadataService;
import com.example.financialdataapp.application.service.metadata.JsonParserService;
import com.example.financialdataapp.domain.model.DocumentId;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class DocumentMetadataFetchUseCase {

    private final EdinetDocumentListService edinetDocumentListService;
    private final JsonParserService jsonParserService;
    private final DocumentMetadataService documentMetadataService;

    public DocumentMetadataFetchUseCase(EdinetDocumentListService edinetDocumentListService, JsonParserService jsonParserService, DocumentMetadataService documentMetadataService) {
        this.documentMetadataService = documentMetadataService;
        this.edinetDocumentListService = edinetDocumentListService;
        this.jsonParserService = jsonParserService;
    }

    /**
     * 指定された日付から書類一覧（メタデータ）を取得し、保存します。
     *
     * @param fromDate メタデータを取得する開始日
     * @return 保存された書類のIDリスト
     */
    public List<DocumentId> fetchAndSaveDocumentMetadata(LocalDate fromDate) {

        var data = edinetDocumentListService.fetchDocumentList(FetchMode.METADATA_AND_LIST, fromDate);
        // metadataおよびcompany情報を取得
        var processedResponseData = jsonParserService.parseDocumentList(data);

        // TODO サービス層で保存結果用の専用クラスを返すことも検討
        return documentMetadataService.createMetadata(processedResponseData);
    }
}
