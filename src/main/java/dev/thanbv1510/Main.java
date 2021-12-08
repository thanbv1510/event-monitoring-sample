package dev.thanbv1510;

import com.ibm.mq.MQException;
import com.ibm.mq.MQGetMessageOptions;
import com.ibm.mq.MQMessage;
import com.ibm.mq.constants.CMQC;
import dev.thanbv1510.entity.LogEntity;
import dev.thanbv1510.mq.MQConsumer;
import dev.thanbv1510.repository.ILogRepository;
import dev.thanbv1510.repository.impl.LogRepository;
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

public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    private Main() {
        throw new IllegalStateException("Main class");
    }

    public static void main(String[] args) {
        logger.info("==> Starting ...");
        MQConsumer consumer = new MQConsumer();

        consumeMessage(consumer);
    }

    public static void consumeMessage(MQConsumer consumer) {
        ILogRepository logRepository = new LogRepository();
        MQGetMessageOptions gmo = new MQGetMessageOptions();
        gmo.options = CMQC.MQGMO_WAIT + CMQC.MQGMO_FAIL_IF_QUIESCING;
        gmo.waitInterval = 5000;  // wait up to 5 seconds
        boolean getMore = true;

        while (getMore) {
            try {
                Optional<MQMessage> mqMessageOptional = consumer.getMessage();
                if (mqMessageOptional.isPresent()) {
                    MQMessage message = mqMessageOptional.get();
                    int strLen = message.getDataLength();
                    byte[] strData = new byte[strLen];
                    message.readFully(strData);
                    String data = new String(strData);

                    LogEntity logEntity = LogEntity.builder()
                            .msg(queryDataFromXMLDocument(data, "/event/bitstreamData/bitstream", null))
                            .flow(queryDataFromXMLDocument(data, "/event/eventPointData/messageFlowData/messageFlow", "wmb:uniqueFlowName"))
                            .nodeLabel(queryDataFromXMLDocument(data, "/event/eventPointData/messageFlowData/node", "wmb:nodeLabel"))
                            .timeReceive(queryDataFromXMLDocument(data, "/event/eventPointData/eventData/eventSequence", "wmb:creationTime"))
                            .nodeType(queryDataFromXMLDocument(data, "/event/eventPointData/messageFlowData/node", "wmb:nodeType"))
                            .build();
                    logRepository.save(logEntity);
                }


            } catch (MQException e) {
                if ((e.completionCode == CMQC.MQCC_FAILED) &&
                        (e.reasonCode == CMQC.MQRC_NO_MSG_AVAILABLE)) {
                    logger.info("==> No message - loop again");
                } else {
                    getMore = false;
                }
            } catch (IOException e) {
                logger.error("==> Ex: ", e);
            }
        }
    }

    private static String queryDataFromXMLDocument(String xmlString, String expression, String attributeName) {
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

            Document doc = dBuilder.parse(new InputSource(new StringReader(xmlString)));
            doc.getDocumentElement().normalize();

            XPath xPath = XPathFactory.newInstance().newXPath();

            NodeList nodeList = (NodeList) xPath.compile(expression).evaluate(
                    doc, XPathConstants.NODESET);

            if (nodeList == null || nodeList.getLength() == 0) {
                return "";
            }

            Node nNode = nodeList.item(0);
            if (attributeName != null && !attributeName.isEmpty() && nNode.getNodeType() == Node.ELEMENT_NODE) {
                Element eElement = (Element) nNode;
                return eElement.getAttribute(attributeName);
            }
            return nNode.getTextContent();
        } catch (ParserConfigurationException | SAXException | IOException | XPathExpressionException e) {
            logger.error("==> Ex: ", e);
            return "";
        }
    }
}
