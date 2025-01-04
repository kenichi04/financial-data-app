package com.example.cash_ratio_analyzer_test.service;

import com.example.cash_ratio_analyzer_test.entity.XbrlData;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class XbrlParserService {

    private DocumentBuilder documentBuilder;

    public XbrlParserService() throws ParserConfigurationException {
        var factory = DocumentBuilderFactory.newInstance();
        this.documentBuilder = factory.newDocumentBuilder();
    }

    // TODO SAXパーサーで逐次解析の方がメモリ効率が良い
    public List<XbrlData> parseXbrl(byte[] xbrlContent) {
        var result = new ArrayList<XbrlData>();

        var document = parseDocumentToDom(xbrlContent);
        var elements = document.getDocumentElement();
        // TODO タグについては要検討
        var nodeList = elements.getElementsByTagName("ix:nonFraction");

        if (nodeList.getLength() == 0) {
            return result;
        }

        // TODO ここで取得する情報を選別する
        for (int i = 0; i < nodeList.getLength(); i++) {
            var element = (Element) nodeList.item(i);
            var xbrlData = extractXbrlData(element);

            if (xbrlData == null) {
                continue;
            }
            result.add(xbrlData);
        }

        return result;
    }

    private XbrlData extractXbrlData(Element element) {
        var name = element.getAttribute("name");
        if (name.isEmpty() || !name.contains("CashAndDeposits")) {
            return null;
        }
        var contextRef = element.getAttribute("contextRef");
        var unitRef = element.getAttribute("unitRef");
        var value = element.getTextContent();

        return new XbrlData(name, contextRef, unitRef, value);
    }

    /**
     * XBRLコンテンツをDOMドキュメントに解析します。
     *
     * @param xbrlContent 解析するXBRLコンテンツのバイト配列
     * @return 解析されたDOMドキュメント
     * @throws RuntimeException XBRLコンテンツの解析に失敗した場合
     */
    private Document parseDocumentToDom(byte[] xbrlContent) {
        try {
            return documentBuilder.parse(new ByteArrayInputStream(xbrlContent));
        } catch (SAXException | IOException e) {
            throw new RuntimeException("Failed to parse XBRL content: " + e.getMessage());
        }
    }
}
