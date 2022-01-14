package dev.thanbv1510.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.IOException;
import java.io.StringReader;
import java.util.Optional;

public class XmlUtils {
    private static final Logger logger;

    static {
        logger = LoggerFactory.getLogger(XmlUtils.class);
    }

    private XmlUtils() {
        throw new IllegalStateException("Util class");
    }

    public static Optional<String> queryDataFromXMLDocument(String xmlString, String expression, String attributeName) {
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

            Document doc = dBuilder.parse(new InputSource(new StringReader(xmlString)));
            doc.getDocumentElement().normalize();

            XPath xPath = XPathFactory.newInstance().newXPath();

            NodeList nodeList = (NodeList) xPath.compile(expression).evaluate(
                    doc, XPathConstants.NODESET);

            if (nodeList == null || nodeList.getLength() == 0) {
                return Optional.empty();
            }

            Node nNode = nodeList.item(0);
            if (attributeName != null && !attributeName.isEmpty() && nNode.getNodeType() == Node.ELEMENT_NODE) {
                Element eElement = (Element) nNode;
                return Optional.of(eElement.getAttribute(attributeName));
            }
            return Optional.of(nNode.getTextContent());
        } catch (ParserConfigurationException | SAXException | IOException | XPathExpressionException e) {
            logger.error("==> Ex: ", e);
            return Optional.empty();
        }
    }
}
