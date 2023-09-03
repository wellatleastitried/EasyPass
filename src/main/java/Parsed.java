package com.walit.pass;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class Parsed {

    private String str = "NO";
    private String pad = "NO";

    public Parsed() throws Exception {
        File file = new File("resources\\utilities\\data\\parsed.xml");
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(file);
        doc.getDocumentElement().normalize();
        NodeList nodes = doc.getElementsByTagName("info");
        Node node = nodes.item(0);
        if (node.getNodeType() == Node.ELEMENT_NODE) {
            Element elem = (Element) node;
            this.str = elem.getElementsByTagName("str").item(0).getTextContent();
            this.pad = elem.getElementsByTagName("pad").item(0).getTextContent();
        }
    }

    protected String getStr() {
        return str;
    }

    protected String getPad() {
        return pad;
    }
}
