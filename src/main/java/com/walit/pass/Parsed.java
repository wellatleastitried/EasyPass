package com.walit.pass;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

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
    private String version;
    private String nameOfProd;

    /**
     * Constructor for Parsed class, initializes str and pad.
     * @throws ParserConfigurationException Thrown if there is an error with the xml document.
     * @throws IOException Thrown if errors finding the xml file come up.
     * @throws SAXException Thrown if there are any remaining errors with the document builder.
     */
    protected Parsed() throws ParserConfigurationException, IOException, SAXException {
        File file = new File("resources\\utilities\\data\\vStaH");
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(file);
        doc.getDocumentElement().normalize();
        NodeList nodes = doc.getElementsByTagName("info");
        Node node = nodes.item(0);
        if (node.getNodeType() == Node.ELEMENT_NODE) {
            Element elem = (Element) node;
            this.str = elem.getElementsByTagName("str").item(0).getTextContent();
            this.version = elem.getElementsByTagName("version").item(0).getTextContent();
            this.nameOfProd = elem.getElementsByTagName("prod").item(0).getTextContent();
        }
    }
    protected String getNameOfProd() { return nameOfProd; }
    /**
     * @return Returns the String str.
     */
    protected String getStr() {
        return new String(deHex(str));
    }
    private byte[] deHex(String string) {
        byte[] cipherText = new byte[string.length() / 2];
        for (int i = 0; i < cipherText.length; i++) {
            int index = i * 2;
            int val = Integer.parseInt(string.substring(index, index + 2), 16);
            cipherText[i] = (byte) val;
        }
        return cipherText;
    }
    /**
     * @return Returns the version information for the program.
     */
    protected String getVersion() { return nameOfProd + "-" + version; }
}
