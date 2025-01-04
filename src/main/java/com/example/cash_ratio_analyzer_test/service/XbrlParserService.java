package com.example.cash_ratio_analyzer_test.service;

import org.springframework.stereotype.Service;
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
    public List<XbrlData> parseXbrl(byte[] xbrlContent) throws IOException, SAXException {
        var result = new ArrayList<XbrlData>();

        var document = documentBuilder.parse(
                new ByteArrayInputStream(xbrlContent));
        var elements = document.getDocumentElement();
        var nodeList = elements.getElementsByTagName("ix:nonFraction");

        if (nodeList.getLength() == 0) {
            return null;
        }

        // TODO ここで取得する情報を選別する
        for (int i = 0; i < nodeList.getLength(); i++) {
            var element = (Element) nodeList.item(i);

            var name = element.getAttribute("name");
            if (name.isEmpty() || !name.contains("CashAndDeposits")) {
                continue;
            }
            var contextRef = element.getAttribute("contextRef");
            var unitRef = element.getAttribute("unitRef");
            var value = element.getTextContent();

            var xbrlData = new XbrlData(name, contextRef, unitRef, value);
            result.add(xbrlData);
        }

        return result;
    }

    public static class XbrlData {
        private String name;
        private String contextRef;
        private String unitRef;
        private String value;

        public XbrlData(String name, String contextRef, String unitRef, String value) {
            this.name = name;
            this.contextRef = contextRef;
            this.unitRef = unitRef;
            this.value = value;
        }

        public String getName() {
            return name;
        }

        public String getContextRef() {
            return contextRef;
        }

        public String getUnitRef() {
            return unitRef;
        }

        public String getValue() {
            return value;
        }
    }
}
