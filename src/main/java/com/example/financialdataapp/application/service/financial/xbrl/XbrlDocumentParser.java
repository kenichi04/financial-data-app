package com.example.financialdataapp.application.service.financial.xbrl;

import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.ByteArrayInputStream;
import java.io.IOException;

/**
 * XBRLコンテンツをDOMドキュメントに解析するクラスです。
 */
@Component
public class XbrlDocumentParser {

    private DocumentBuilder documentBuilder;

    public XbrlDocumentParser() throws ParserConfigurationException {
        var factory = DocumentBuilderFactory.newInstance();
        this.documentBuilder = factory.newDocumentBuilder();
    }

    /**
     * XBRLコンテンツをDOMドキュメントに解析します。
     *
     * @param xbrlContent 解析するXBRLコンテンツのバイト配列
     * @return 解析されたDOMドキュメント
     * @throws RuntimeException XBRLコンテンツの解析に失敗した場合
     */
    public Document parseDocumentToDom(byte[] xbrlContent) {
        try {
            return documentBuilder.parse(new ByteArrayInputStream(xbrlContent));
        } catch (SAXException | IOException e) {
            throw new RuntimeException("Failed to parse XBRL content: " + e.getMessage());
        }
    }
}
