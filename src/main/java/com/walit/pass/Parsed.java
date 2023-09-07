package com.walit.pass;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import java.io.File;
import java.io.IOException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * Parsed serves as a xml parser to extract data for the program's startup.
 *
 * @author Jackson Swindell
 */
class Parsed {

    private String str;
    private String pad;

    /**
     * Constructor for Parsed class, initializes str and pad.
     * @throws ParserConfigurationException Thrown if there is an error with the xml document.
     * @throws IOException Thrown if errors finding the xml file come up.
     * @throws SAXException Thrown if there are any remaining errors with the document builder.
     */
    public Parsed() throws ParserConfigurationException, IOException, SAXException {
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

    /**
     * @return Returns the String str.
     */
    protected String getStr() {
        return str;
    }

    /**
     * @return Returns the String pad.
     */
    protected String getPad() {
        return pad;
    }
}
